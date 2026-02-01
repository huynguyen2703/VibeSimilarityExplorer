package com.huynguyen2703.songs.vibeexplorer.functional_tests.unit_tests.etl_tests;

import com.huynguyen2703.songs.vibeexplorer.etl.SpotifyLoadService;
import com.huynguyen2703.songs.vibeexplorer.models.Song;
import com.huynguyen2703.songs.vibeexplorer.repositories.SongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for SpotifyLoadService using Testcontainers
 * Tests real database operations with PostgreSQL
 */
@SpringBootTest
@Testcontainers
public class SpotifyLoadServiceTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private SpotifyLoadService loadService;

    @Autowired
    private SongRepository songRepository;

    @BeforeEach
    void setUp() {
        // Clean database before each test
        songRepository.deleteAll();
    }

    @Test
    void testLoadSongsSuccessfully() {
        // Given: 3 new songs
        List<Song> songs = List.of(
                createSong("spotify1", "Song One", "Artist A", "Album A", 2023),
                createSong("spotify2", "Song Two", "Artist B", "Album B", 2024),
                createSong("spotify3", "Song Three", "Artist C", "Album C", 2025)
        );

        // When: Load them
        loadService.loadSongs(songs, 50);

        // Then: All 3 should be in database
        List<Song> savedSongs = songRepository.findAll();
        assertEquals(3, savedSongs.size(), "Should save all 3 songs");

        // Verify specific song exists
        assertTrue(songRepository.findBySpotifyId("spotify1").isPresent());
        assertTrue(songRepository.findBySpotifyId("spotify2").isPresent());
        assertTrue(songRepository.findBySpotifyId("spotify3").isPresent());
    }

    @Test
    void testLoadSongsDeduplicatesInMemory() {
        // Given: 5 songs, but 2 are duplicates (same Spotify ID)
        List<Song> songs = List.of(
                createSong("spotify1", "Song One", "Artist A", "Album A", 2023),
                createSong("spotify2", "Song Two", "Artist B", "Album B", 2024),
                createSong("spotify1", "Song One Duplicate", "Artist A", "Album A", 2023), // Duplicate!
                createSong("spotify3", "Song Three", "Artist C", "Album C", 2025),
                createSong("spotify2", "Song Two Duplicate", "Artist B", "Album B", 2024)  // Duplicate!
        );

        // When: Load them
        loadService.loadSongs(songs, 50);

        // Then: Only 3 unique songs should be saved
        List<Song> savedSongs = songRepository.findAll();
        assertEquals(3, savedSongs.size(), "Should deduplicate in memory");
    }

    @Test
    void testLoadSongsSkipsExistingInDatabase() {
        // Given: 2 songs already in database
        Song existingSong1 = createSong("spotify1", "Existing Song 1", "Artist A", "Album A", 2023);
        Song existingSong2 = createSong("spotify2", "Existing Song 2", "Artist B", "Album B", 2024);
        songRepository.saveAll(List.of(existingSong1, existingSong2));

        // When: Try to load 4 songs (2 existing + 2 new)
        List<Song> songs = List.of(
                createSong("spotify1", "Existing Song 1", "Artist A", "Album A", 2023), // Already exists
                createSong("spotify2", "Existing Song 2", "Artist B", "Album B", 2024), // Already exists
                createSong("spotify3", "New Song 1", "Artist C", "Album C", 2025),      // New
                createSong("spotify4", "New Song 2", "Artist D", "Album D", 2026)       // New
        );

        loadService.loadSongs(songs, 50);

        // Then: Should only have 4 total (2 existing + 2 new)
        List<Song> allSongs = songRepository.findAll();
        assertEquals(4, allSongs.size(), "Should skip existing songs");
    }

    @Test
    void testLoadSongsBatchInserts() {
        // Given: 10 songs with batch size of 3
        List<Song> songs = List.of(
                createSong("s1", "Song 1", "Artist 1", "Album 1", 2020),
                createSong("s2", "Song 2", "Artist 2", "Album 2", 2021),
                createSong("s3", "Song 3", "Artist 3", "Album 3", 2022),
                createSong("s4", "Song 4", "Artist 4", "Album 4", 2023),
                createSong("s5", "Song 5", "Artist 5", "Album 5", 2024),
                createSong("s6", "Song 6", "Artist 6", "Album 6", 2020),
                createSong("s7", "Song 7", "Artist 7", "Album 7", 2021),
                createSong("s8", "Song 8", "Artist 8", "Album 8", 2022),
                createSong("s9", "Song 9", "Artist 9", "Album 9", 2023),
                createSong("s10", "Song 10", "Artist 10", "Album 10", 2024)
        );

        // When: Load with batch size 3
        loadService.loadSongs(songs, 3);

        // Then: All 10 should be saved (batches: 3+3+3+1)
        assertEquals(10, songRepository.count(), "Should save all songs in batches");
    }

    @Test
    void testLoadSongsSkipsInvalidSpotifyIds() {
        // Given: Songs with invalid Spotify IDs
        List<Song> songs = List.of(
                createSong("spotify1", "Valid Song", "Artist A", "Album A", 2023),
                createSong(null, "Null ID Song", "Artist B", "Album B", 2024),           // Invalid
                createSong("", "Empty ID Song", "Artist C", "Album C", 2025),             // Invalid
                createSong("Unknown", "Unknown ID Song", "Artist D", "Album D", 2026),    // Invalid
                createSong("spotify2", "Valid Song 2", "Artist E", "Album E", 2027)
        );

        // When: Load them
        loadService.loadSongs(songs, 50);

        // Then: Only 2 valid songs should be saved
        assertEquals(2, songRepository.count(), "Should skip invalid Spotify IDs");
    }

    @Test
    void testLoadSongsWithNullOrEmptyList() {
        // When: Load null
        loadService.loadSongs(null, 50);

        // Then: No crash, no songs saved
        assertEquals(0, songRepository.count());

        // When: Load empty list
        loadService.loadSongs(List.of(), 50);

        // Then: Still no songs
        assertEquals(0, songRepository.count());
    }

    // ========== Helper Methods ==========

    private Song createSong(String spotifyId, String title, String artist, String album, Integer year) {
        return new Song(
                spotifyId,
                null,  // songGraph
                null,  // songCluster
                title,
                artist,
                album,
                null,  // genre
                year
        );
    }
}
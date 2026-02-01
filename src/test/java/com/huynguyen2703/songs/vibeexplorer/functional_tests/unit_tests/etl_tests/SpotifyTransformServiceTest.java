package com.huynguyen2703.songs.vibeexplorer.functional_tests.unit_tests.etl_tests;


import com.huynguyen2703.songs.vibeexplorer.etl.SpotifyTransformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.BeforeEach;

import com.huynguyen2703.songs.vibeexplorer.dto.spotify.SpotifyAlbumDto;
import com.huynguyen2703.songs.vibeexplorer.dto.spotify.SpotifyArtistDto;
import com.huynguyen2703.songs.vibeexplorer.dto.spotify.SpotifyTrackDto;
import com.huynguyen2703.songs.vibeexplorer.dto.spotify.SpotifyTracksDto;
import com.huynguyen2703.songs.vibeexplorer.models.Song;


import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SpotifyTransformServiceTest {

    private SpotifyTransformService spotifyTransformService;

    @BeforeEach
    void setUp() {
        spotifyTransformService = new SpotifyTransformService();
    }

    @Test
    void testTransformTracksWithValidData() {
        SpotifyTracksDto dto = createMockSpotifyTracksDto(
                "track123",
                "Beautiful Song",
                "Artist Name",
                "Album Name",
                2023
        );
        List<Song> songs = spotifyTransformService.transformTracks(dto);

        assertEquals(1, songs.size(), "Expected one song");

        Song song = songs.get(0);

        assertEquals("track123", song.getSpotifyId());
        assertEquals("Beautiful Song", song.getTitle());
        assertEquals("Artist Name", song.getArtist());
        assertEquals("Album Name", song.getAlbum());
        assertEquals(2023, song.getReleaseYear());
        assertNull(song.getGenre(), "Genre should be null (not extracted yet)");
    }

    @Test
    void testTransformTracksWithMultipleArtists() {
        // Given: Track with multiple artists
        SpotifyArtistDto artist1 = new SpotifyArtistDto("artist1", "Taylor Swift");
        SpotifyArtistDto artist2 = new SpotifyArtistDto("artist2", "Ed Sheeran");
        SpotifyAlbumDto album = new SpotifyAlbumDto(
                "album1",
                "Collaboration Album",
                "2024",    // release_date
                "year"     // release_date_precision
        );

        SpotifyTrackDto track = new SpotifyTrackDto(
                "track456",
                "Duet Song",
                "180000",
                85,
                album,
                2024,
                List.of(artist1, artist2)
        );

        SpotifyTracksDto dto = new SpotifyTracksDto(List.of(track), 10, 0, 1);

        List<Song> songs = spotifyTransformService.transformTracks(dto);

        assertEquals("Taylor Swift", songs.get(0).getArtist());
    }

    @Test
    void testTransformTracksSkipsMissingSpotifyId() {
        // Given: Track without Spotify ID
        SpotifyArtistDto artist = new SpotifyArtistDto("artist1", "Artist");
        SpotifyAlbumDto album = new SpotifyAlbumDto(
                "album1",
                "Album",
                "2023",    // release_date
                "year"     // release_date_precision
        );

        SpotifyTrackDto validTrack = new SpotifyTrackDto("track1", "Valid", "180000", 80, album, 2023, List.of(artist));
        SpotifyTrackDto invalidTrack = new SpotifyTrackDto(null, "Invalid", "180000", 80, album, 2023, List.of(artist));
        SpotifyTrackDto emptyIdTrack = new SpotifyTrackDto("", "Empty", "180000", 80, album, 2023, List.of(artist));

        SpotifyTracksDto dto = new SpotifyTracksDto(List.of(validTrack, invalidTrack, emptyIdTrack), 10, 0, 3);

        // When
        List<Song> songs = spotifyTransformService.transformTracks(dto);

        // Then: Should only include valid track
        assertEquals(1, songs.size(), "Should skip tracks without valid Spotify ID");
        assertEquals("track1", songs.get(0).getSpotifyId());
    }

    @Test
    void testTransformTracksWithMissingArtist() {
        // Given: Track with no artists
        SpotifyTracksDto dto = createMockSpotifyTracksDtoWithoutArtist("track789", "Solo Track");

        // When
        List<Song> songs = spotifyTransformService.transformTracks(dto);

        // Then: Should use "Unknown"
        assertEquals("Unknown", songs.get(0).getArtist());
    }


    private SpotifyTracksDto createMockSpotifyTracksDto(
            String trackId,
            String trackName,
            String artistName,
            String albumName,
            Integer releaseYear
    ) {
        SpotifyArtistDto artist = new SpotifyArtistDto("artist1", artistName);
        SpotifyAlbumDto album = new SpotifyAlbumDto(
                "album1",
                albumName,
                releaseYear.toString(),  // release_date as string
                "year"                    // release_date_precision
        );

        SpotifyTrackDto track = new SpotifyTrackDto(
                trackId,
                trackName,
                "180000",
                85,
                album,
                releaseYear,
                List.of(artist)
        );

        return new SpotifyTracksDto(List.of(track), 10, 0, 1);
    }

    private SpotifyTracksDto createMockSpotifyTracksDtoWithoutArtist(String trackId, String trackName) {
        SpotifyAlbumDto album = new SpotifyAlbumDto(
                "album1",
                "Album",
                "2023",     // release_date
                "year"      // release_date_precision
        );

        SpotifyTrackDto track = new SpotifyTrackDto(
                trackId,
                trackName,
                "180000",
                85,
                album,
                2023,
                List.of()  // No artists
        );

        return new SpotifyTracksDto(List.of(track), 10, 0, 1);
    }
}

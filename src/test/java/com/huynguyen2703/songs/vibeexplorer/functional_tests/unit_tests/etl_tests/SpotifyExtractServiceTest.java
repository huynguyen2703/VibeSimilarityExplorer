package com.huynguyen2703.songs.vibeexplorer.functional_tests.unit_tests.etl_tests;

import com.huynguyen2703.songs.vibeexplorer.dto.spotify.SpotifyTracksDto;
import com.huynguyen2703.songs.vibeexplorer.etl.SpotifyExtractService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test hitting REAL Spotify API
 * Fast, real data, good enough for personal projects
 */
@SpringBootTest()
public class SpotifyExtractServiceTest {

    @Autowired
    private SpotifyExtractService spotifyExtractService;

    @Test
    void testRealSpotifyExtraction() {
        // Real API call with real data
        SpotifyTracksDto result = spotifyExtractService
                .extractTracks("ballad", "VN", 10, 0)
                .block();

        // Verify what actually matters
        assertNotNull(result, "Should get data from Spotify");
        assertFalse(result.items().isEmpty(), "Should have actual tracks");
        assertTrue(result.total() > 0, "Should have total count");
    }

    @Test
    void testInvalidQueryHandling() {
        // Test edge case: empty query
        assertThrows(
                RuntimeException.class,
                () -> spotifyExtractService.extractTracks("", "VN", 10, 0).block()
        );
    }
}
package com.huynguyen2703.songs.vibeexplorer.etl;
import com.huynguyen2703.songs.vibeexplorer.dto.spotify.SpotifyTracksDto;
import org.springframework.stereotype.Component;
import com.huynguyen2703.songs.vibeexplorer.models.Song;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Orchestrates the full Spotify ETL flow: Extract → Transform → Load.
 * <p>
 * This component manages the complete pipeline:
 * <ul>
 *   <li>Extract raw track data from Spotify API</li>
 *   <li>Transform DTOs into domain {@link Song} entities</li>
 *   <li>Load transformed entities into the database using {@link SpotifyLoadService}</li>
 * </ul>
 * <p>
 * Includes error handling, retry logic, logging, and respects configuration defined
 * in {@link SpotifyEtlConfig}.
 */
@Component
public class SpotifyEtlJob {
    private final SpotifyEtlConfig spotifyEtlConfig;
    private final SpotifyLoadService spotifyLoadService;
    private final SpotifyTransformService spotifyTransformService;
    private final SpotifyExtractService spotifyExtractService;
    private static final int RETRIES = 2;

    public SpotifyEtlJob(
            SpotifyEtlConfig config,
            SpotifyLoadService spotifyLoadService,
            SpotifyTransformService spotifyTransformService,
            SpotifyExtractService spotifyExtractService
    ) {
        this.spotifyEtlConfig = config;
        this.spotifyLoadService = spotifyLoadService;
        this.spotifyTransformService = spotifyTransformService;
        this.spotifyExtractService = spotifyExtractService;
    }

    public void runEtl() {
        System.out.println("=== Starting Spotify ETL Run ===");

        try {
            for (String keyword: spotifyEtlConfig.getDefaultKeywords()) {
                System.out.println("Fetching tracks for keyword: " + keyword);

                // Fetch up to maxPages
                for (int page = 0; page < spotifyEtlConfig.getMaxPages(); page++) {
                    System.out.println("Fetching page " + (page + 1));

                    int offset = page * spotifyEtlConfig.getBatchSize();
                    int limit = spotifyEtlConfig.getBatchSize();
                    String market = spotifyEtlConfig.getMarket();


                    // Extract Service
                    int finalPage = page;
                    SpotifyTracksDto rawTracks = spotifyExtractService.extractTracks(keyword, market, limit, offset)
                            .retry(RETRIES)
                            .onErrorResume(e -> {
                                System.err.println("API call failed for keyword=" + keyword + ", page=" + (finalPage + 1) + ": " + e.getMessage());
                                return Mono.empty();
                            })
                            .block();

                    if (rawTracks == null) {
                        System.err.println("No data found for keyword: " + keyword);
                        continue;
                    }

                    // Transform Service
                    List<Song> songs = spotifyTransformService.transformTracks(rawTracks);

                    // Load Service
                    spotifyLoadService.loadSongs(songs, spotifyEtlConfig.getBatchSize());
                }
            }
            System.out.println("=== Spotify ETL Run Completed ===");
        } catch (Exception e) {
            System.err.println("Error during ETL run: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

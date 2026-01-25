package com.huynguyen2703.songs.vibeexplorer.etl;
import com.huynguyen2703.songs.vibeexplorer.dto.spotify.SpotifyTracksDto;
import com.huynguyen2703.songs.vibeexplorer.services.externals.SpotifyApiService;
import com.huynguyen2703.songs.vibeexplorer.services.externals.SpotifyAuthService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service responsible for communicating with the Spotify Web API to fetch raw track data.
 * <p>
 * Responsibilities:
 * <ul>
 *     <li>Authenticate using {@link SpotifyAuthService}</li>
 *     <li>Query Spotify API for tracks, artists, and albums using {@link SpotifyApiService}</li>
 *     <li>Support pagination via limit and offset parameters</li>
 *     <li>Return Spotify DTOs (not JPA entities)</li>
 *     <li>Designed for reactive, non-blocking calls via {@link Mono}</li>
 * </ul>
 * <p>
 * TODOs:
 * <ul>
 *     <li>Handle Spotify rate limits and retries</li>
 *     <li>Enhance error handling for failed API calls</li>
 * </ul>
 */
@Service
public class SpotifyExtractService {
    SpotifyAuthService spotifyAuthService;
    SpotifyApiService spotifyApiService;


    public SpotifyExtractService(SpotifyAuthService spotifyAuthService, SpotifyApiService spotifyApiService) {
        this.spotifyAuthService = spotifyAuthService;
        this.spotifyApiService = spotifyApiService;
    }

    public Mono<SpotifyTracksDto> extractTracks(String query, String market, int limit, int offset) {
        return this.spotifyAuthService.getToken()
                .flatMap(spotifyToken -> this.spotifyApiService.searchTracks(spotifyToken, query, market, limit, offset));
    }
}

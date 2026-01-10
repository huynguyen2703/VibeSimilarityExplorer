package com.huynguyen2703.songs.vibeexplorer.etl;
import com.huynguyen2703.songs.vibeexplorer.dto.spotify.SpotifyTracksDto;
import com.huynguyen2703.songs.vibeexplorer.services.externals.SpotifyApiService;
import com.huynguyen2703.songs.vibeexplorer.services.externals.SpotifyAuthService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


/*
    Responsibility:

    Communicate with Spotify Web API

    Fetch raw JSON / DTOs (tracks, artists, albums)

    TODOs:

    Inject SpotifyAuthService

    Inject WebClient (Spotify an API client)

    Implement search-by-keyword extraction

    Support pagination (limit, offset)

    Return Spotify DTOs (NOT entities)

    Handle rate limits & retries (later)
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

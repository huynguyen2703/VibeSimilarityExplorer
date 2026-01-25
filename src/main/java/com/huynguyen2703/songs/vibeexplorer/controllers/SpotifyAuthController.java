package com.huynguyen2703.songs.vibeexplorer.controllers;

import com.huynguyen2703.songs.vibeexplorer.dto.spotify.SpotifyTracksDto;
import com.huynguyen2703.songs.vibeexplorer.services.externals.SpotifyAuthService;
import com.huynguyen2703.songs.vibeexplorer.services.externals.SpotifyApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class SpotifyAuthController {

    private final SpotifyAuthService spotifyAuthService;
    private final SpotifyApiService spotifyApiService;

    public SpotifyAuthController(SpotifyAuthService spotifyAuthService, SpotifyApiService spotifyApiService) {
        this.spotifyAuthService = spotifyAuthService;
        this.spotifyApiService = spotifyApiService;
    }

    // 1️⃣ Test getting a token directly
    @GetMapping("/spotify/token")
    public Mono<String> getSpotifyToken() {
        return spotifyAuthService.getToken();
    }

    // 2️⃣ Test searching for tracks on Spotify
    @GetMapping("/spotify/search")
    public Mono<SpotifyTracksDto> searchTrack(@RequestParam String query, @RequestParam String market, @RequestParam int limit, @RequestParam int offset) {
        return spotifyAuthService.getToken()
                .flatMap(token -> spotifyApiService.searchTracks(token, query, market, limit, offset));
    }
}

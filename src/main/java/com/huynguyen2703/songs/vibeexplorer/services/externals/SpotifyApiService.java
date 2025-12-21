package com.huynguyen2703.songs.vibeexplorer.services.externals;

import com.huynguyen2703.songs.vibeexplorer.dto.spotify.SpotifyTracksDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class SpotifyApiService {
    private final WebClient webClient;

    public SpotifyApiService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://api.spotify.com/v1").build();
    }

    public Mono<SpotifyTracksDto> searchTracks(String token, String query, String market, int limit, int offset) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("q", query)
                        .queryParam("type", "track")
                        .queryParam("market", market.trim().toUpperCase())
                        .queryParam("limit", limit)
                        .queryParam("offset", offset)
                        .build())
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(SpotifyTracksDto.class)
                .onErrorResume(WebClientResponseException.class, e ->
                        Mono.error(new RuntimeException("Spotify API Error " + e.getResponseBodyAsString())));
    }
}

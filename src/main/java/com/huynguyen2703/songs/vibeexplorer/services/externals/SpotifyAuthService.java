package com.huynguyen2703.songs.vibeexplorer.services.externals;

import com.huynguyen2703.songs.vibeexplorer.dto.spotify.SpotifyTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;

import java.util.Base64;

@Service
public class SpotifyAuthService {

    private final WebClient webClient;
    private final String clientId;
    private final String clientSecret;

    // Token cache
    private Mono<String> cachedToken;
    private Instant tokenExpiry;

    public SpotifyAuthService(
            WebClient.Builder builder,
            @Value("${spotify.client-id}") String clientId,
            @Value("${spotify.client-secret}") String clientSecret
    ) {
        this.webClient = builder
                .baseUrl("https://accounts.spotify.com")
                .build();
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public Mono<String> getToken() {
        // Reuse token if still valid
        if (cachedToken != null && tokenExpiry != null && tokenExpiry.isBefore(Instant.now())) {
            return cachedToken;
        }

        // after token returned, Mono<String> return type turns to String
        return requestNewToken()
                .doOnNext(token -> {
                    this.cachedToken = Mono.just(token);
                    this.tokenExpiry = Instant.now().plusSeconds(3500);
                });
    }

    private Mono<String> requestNewToken() {
        return webClient.post()
                .uri("api/token")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + buildBasicAuthHeader())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "client_credentials"))
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class)
                                .map(body -> new RuntimeException("Spotify auth 4xx: " + body))
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RuntimeException("Spotify auth 5xx"))
                )
                .bodyToMono(SpotifyTokenResponse.class)
                .timeout(Duration.ofSeconds(5))
                .map(SpotifyTokenResponse::access_token);
    }

    private String buildBasicAuthHeader() {
        return Base64.getEncoder()
                .encodeToString((clientId + ":" + clientSecret)
                        .getBytes(StandardCharsets.UTF_8));
    }
}




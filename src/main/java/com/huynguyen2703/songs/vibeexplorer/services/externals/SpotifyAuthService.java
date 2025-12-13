package com.huynguyen2703.songs.vibeexplorer.services.externals;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Base64;

import java.util.Map;

@Service
public class SpotifyAuthService {
    private final WebClient webClient;
    private final String clientId;
    private final String clientSecret;

    public SpotifyAuthService(
            WebClient.Builder builder,
            @Value("${spotify.client-id}") String clientId,
            @Value("${spotify.client-secret}")String clientSecret) {
        this.webClient = builder.baseUrl("https://accounts.spotify.com/api/token").build();
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public Mono<String> getToken() {
        String basicAuth = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes(java.nio.charset.StandardCharsets.UTF_8));

        return webClient.post()
                .header("Authorization", "Basic " + basicAuth)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue("grant_type=client_credentials")
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (String) response.get("access_token"));
    }
}

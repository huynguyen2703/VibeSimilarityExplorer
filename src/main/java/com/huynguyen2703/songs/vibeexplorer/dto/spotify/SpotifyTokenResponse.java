package com.huynguyen2703.songs.vibeexplorer.dto.spotify;

public record SpotifyTokenResponse(
        String access_token,
        String token_type,
        Integer expires_in
) {}
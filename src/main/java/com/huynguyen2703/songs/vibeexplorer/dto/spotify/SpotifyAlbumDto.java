package com.huynguyen2703.songs.vibeexplorer.dto.spotify;

public record SpotifyAlbumDto (
        String id,
        String name,
        String release_date,
        String release_date_precision
) {}
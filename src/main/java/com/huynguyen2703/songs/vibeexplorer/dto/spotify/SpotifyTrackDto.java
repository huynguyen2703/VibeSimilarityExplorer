package com.huynguyen2703.songs.vibeexplorer.dto.spotify;

import java.util.List;

public record SpotifyTrackDto (
        String id,
        String name,
        String duration_ms,
        Integer popularity,
        SpotifyAlbumDto album,
        List<SpotifyArtistDto> artists
) {}

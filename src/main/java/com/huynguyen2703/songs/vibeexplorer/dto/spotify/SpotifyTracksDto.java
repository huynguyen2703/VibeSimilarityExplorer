package com.huynguyen2703.songs.vibeexplorer.dto.spotify;

import java.util.List;

public record SpotifyTracksDto (
        List<SpotifyTrackDto> items,
        Integer limit,
        Integer offset,
        Integer total
) {}

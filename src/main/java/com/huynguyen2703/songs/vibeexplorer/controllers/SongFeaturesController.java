package com.huynguyen2703.songs.vibeexplorer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import com.huynguyen2703.songs.vibeexplorer.services.SongFeaturesService;
import com.huynguyen2703.songs.vibeexplorer.models.SongFeatures;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/song-features")
public class SongFeaturesController {
    private final SongFeaturesService songFeaturesService;

    @Autowired
    public SongFeaturesController(SongFeaturesService songFeaturesService) {
        this.songFeaturesService = songFeaturesService;
    }

    // TODO: Careful since not all songs in the app can have features ready analyzed
    @GetMapping("/{songId}")
    public SongFeatures getSongFeatures(@PathVariable Long songId) {
        Optional<SongFeatures> songFeatures = songFeaturesService.getSongFeatureBySongId(songId);
        return songFeatures.orElseThrow(() -> new RuntimeException("Song features not found"));
    }

}

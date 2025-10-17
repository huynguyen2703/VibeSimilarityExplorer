package com.huynguyen2703.songs.vibeexplorer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import com.huynguyen2703.songs.vibeexplorer.services.SongService;
import com.huynguyen2703.songs.vibeexplorer.models.Song;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/songs")
public class SongController {
    private final SongService songService;

    @Autowired
    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping
    public List<Song> getAllSongs() {
        return songService.getAllSongs();
    }

    @GetMapping("/{id}")
    public Optional<Song> getSongById(@PathVariable Long id) {
        return songService.findSongById(id);
    }

    @GetMapping("/search")
    public List<Song> searchSong(@RequestParam(required = false) String artist, @RequestParam(required = false) String title) {
        return songService.searchSong(artist, title);
    }
}

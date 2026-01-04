package com.huynguyen2703.songs.vibeexplorer.etl;

import com.huynguyen2703.songs.vibeexplorer.models.Song;
import com.huynguyen2703.songs.vibeexplorer.services.SongService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
    Responsibility:

    Persist transformed entities into the database

    TODOs:

    Inject SongService

    Decide upsert strategy (by Spotify track ID / ISRC)

    Avoid duplicate song insertion

    Handle cascading save (SongFeatures)

    Batch inserts for performance (later)
 */


@Service
public class SpotifyLoadService {
    private final SongService songService;

    public SpotifyLoadService(SongService songService) {
        this.songService = songService;
    }

    /**
     * Persist a list of Songs into the database.
     * Skips duplicates in memory and in DB.
     */
    @Transactional
    public void loadSongs(List<Song> songs) {
        if (songs == null || songs.isEmpty()) return;

        System.out.println("Starting to load " + songs.size() + " songs...");


        // Deduplicate im memory by spotifyId
        Map<String, Song> uniqueSongs = new HashMap<>();
        for (Song song : songs) {
            String spotifyId = song.getSpotifyId();
            if (spotifyId != null && !spotifyId.equals("Unknown") && !uniqueSongs.containsKey(spotifyId)) {
                uniqueSongs.put(song.getSpotifyId(), song);
            } else {
                System.out.println("Skipping duplicate or invalid song: " + song.getTitle());
            }
        }

        List<Song> newSongs = uniqueSongs.values().stream()
                .filter(song -> songService.findBySpotifyId(song.getSpotifyId()).isEmpty())
                .toList();

        System.out.println("Deduplicated songs count: " + uniqueSongs.size());

        // Save all at once
        songService.saveAll(newSongs);
        System.out.println("Persisted " + newSongs.size() + " new songs in batch.");
    }

}

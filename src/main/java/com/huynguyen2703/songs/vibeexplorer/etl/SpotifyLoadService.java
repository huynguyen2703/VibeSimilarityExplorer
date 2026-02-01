package com.huynguyen2703.songs.vibeexplorer.etl;

import com.huynguyen2703.songs.vibeexplorer.models.Song;
import com.huynguyen2703.songs.vibeexplorer.services.SongService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Service responsible for persisting transformed {@link Song} entities into the database.
 * <p>
 * Responsibilities:
 * <ul>
 *     <li>Deduplicate songs in memory based on {@code spotifyId}</li>
 *     <li>Check against the database to avoid duplicate insertions</li>
 *     <li>Perform batch inserts to improve database write performance</li>
 *     <li>Handle cascading save operations (e.g., {@code SongFeatures})</li>
 *     <li>Transactional by default to ensure consistency</li>
 * </ul>
 * <p>
 * TODOs:
 * <ul>
 *     <li>Decide on upsert strategy (by Spotify ID or ISRC)</li>
 *     <li>Enhance error handling for database failures</li>
 * </ul>
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
    public void loadSongs(List<Song> songs, int batchSize) {
        if (songs == null || songs.isEmpty()) return;

        System.out.println("Starting to load " + songs.size() + " songs...");


        // Deduplicate im memory by spotifyId
        Map<String, Song> uniqueSongs = new HashMap<>();
        for (Song song : songs) {
            String spotifyId = song.getSpotifyId();
            if (spotifyId != null && !spotifyId.trim().isEmpty() && !spotifyId.equals("Unknown") && !uniqueSongs.containsKey(spotifyId)) {
                uniqueSongs.put(song.getSpotifyId(), song);
            } else {
                System.out.println("Skipping duplicate or invalid song: " + song.getTitle());
            }
        }

        List<Song> newSongs = uniqueSongs.values().stream()
                .filter(song -> songService.findBySpotifyId(song.getSpotifyId()).isEmpty())
                .toList();

        System.out.println("Deduplicated songs count: " + newSongs.size());

        // Batch Inserts
        for (int startBatch = 0; startBatch < newSongs.size(); startBatch += batchSize) {
            int endBatch = Math.min(startBatch + batchSize, newSongs.size());

            List<Song> batchSongs = newSongs.subList(startBatch, endBatch);
            songService.saveAll(batchSongs);
            System.out.println("Persisted batch " + (startBatch / batchSize + 1) + " with " + batchSongs.size() + " songs.");
        }
    }

}

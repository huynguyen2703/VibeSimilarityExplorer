package com.huynguyen2703.songs.vibeexplorer.services;

import com.huynguyen2703.songs.vibeexplorer.models.Song;
import com.huynguyen2703.songs.vibeexplorer.repositories.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SongService {
    private final SongRepository songRepository;

    @Autowired
    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public Song saveSong(Song song) {
        return songRepository.save(song);
    }

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    public Optional<Song> findSongBySongId(Long id) {
        return songRepository.findById(id);
    }

    public void deleteSong(Song song) {
        songRepository.delete(song);
    }

    public List<Song> searchSong(String artist, String title) {
        if (artist == null && title == null) {
            return songRepository.findAll();
        }
        if (artist == null) {
            return songRepository.findByTitleContainingIgnoreCase(title);
        }
        if (title == null) {
            return songRepository.findByArtistContainingIgnoreCase(artist);
        } else {
            return songRepository.findByArtistContainingIgnoreCaseAndTitleContainingIgnoreCase(artist, title);
        }
    }

    public Optional<Song> findBySpotifyId(String spotifyId) {
        return songRepository.findBySpotifyId(spotifyId);
    }

    public Song saveIfNotExists(Song song) {
        return songRepository
                .findBySpotifyId(song.getSpotifyId())
                .orElseGet(() -> songRepository.save(song));
    }
}

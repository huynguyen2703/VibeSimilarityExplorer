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

    public List<Song> getAllSongs () {
        return songRepository.findAll();
    }

    public Optional<Song> findSongById(Long id) {
        return songRepository.findById(id);
    }

    public void deleteSong(Song song) {
        songRepository.delete(song);
    }
}

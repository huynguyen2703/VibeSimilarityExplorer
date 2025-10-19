package com.huynguyen2703.songs.vibeexplorer.repositories;

import com.huynguyen2703.songs.vibeexplorer.models.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    // JpaRepository already provides basic CRUD methods:
    // save(), findById(), findAll(), deleteById(), etc.

    List<Song> findByArtistContainingIgnoreCase(String artist);

    List<Song> findByTitleContainingIgnoreCase(String title);

    List<Song> findByArtistContainingIgnoreCaseAndTitleContainingIgnoreCase(String artist, String title);
}

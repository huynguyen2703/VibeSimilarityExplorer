package com.huynguyen2703.songs.vibeexplorer.repositories;

import com.huynguyen2703.songs.vibeexplorer.models.SongFeatures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongFeaturesRepository extends JpaRepository<SongFeatures, Long>{
    // JpaRepository already provides basic CRUD methods:
    // save(), findById(), findAll(), deleteById(), etc.
}

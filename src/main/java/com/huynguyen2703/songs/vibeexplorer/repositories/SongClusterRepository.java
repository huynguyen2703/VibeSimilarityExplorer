package com.huynguyen2703.songs.vibeexplorer.repositories;

import com.huynguyen2703.songs.vibeexplorer.models.SongCluster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SongClusterRepository extends JpaRepository<SongCluster, Long> {
    // JpaRepository already provides basic CRUD methods:
    // save(), findById(), findAll(), deleteById(), etc.
}

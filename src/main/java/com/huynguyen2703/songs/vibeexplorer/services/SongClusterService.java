package com.huynguyen2703.songs.vibeexplorer.services;

import com.huynguyen2703.songs.vibeexplorer.models.SongCluster;
import com.huynguyen2703.songs.vibeexplorer.repositories.SongClusterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class SongClusterService {
    private final SongClusterRepository songClusterRepository;

    @Autowired
    public SongClusterService(SongClusterRepository songClusterRepository) {
        this.songClusterRepository = songClusterRepository;
    }

    public SongCluster saveSongCluster(SongCluster SongCluster) {
        return songClusterRepository.save(SongCluster);
    }

    public Optional<SongCluster> findSongClusterById(Long id) {
        return songClusterRepository.findById(id);
    }

    public List<SongCluster> findAllSongClusters() {
        return songClusterRepository.findAll();
    }

    public void deleteSongCluster(SongCluster SongCluster) {
        songClusterRepository.delete(SongCluster);
    }
}

package com.huynguyen2703.songs.vibeexplorer.services;

import com.huynguyen2703.songs.vibeexplorer.models.SongGraph;
import com.huynguyen2703.songs.vibeexplorer.repositories.SongGraphRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class SongGraphService {
    private final SongGraphRepository songGraphRepository;

    @Autowired
    public SongGraphService(SongGraphRepository songGraphRepository) {
        this.songGraphRepository = songGraphRepository;
    }

    public SongGraph saveSongGraph(SongGraph songGraph) {
        return songGraphRepository.save(songGraph);
    }

    public Optional<SongGraph> findSongGraphById(Long id) {
        return songGraphRepository.findById(id);
    }

    public List<SongGraph> findAllSongGraphs() {
        return songGraphRepository.findAll();
    }

    public void deleteSongGraph(SongGraph songGraph) {
        songGraphRepository.delete(songGraph);
    }
}

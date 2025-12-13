package com.huynguyen2703.songs.vibeexplorer.services;

import com.huynguyen2703.songs.vibeexplorer.models.SongFeatures;
import com.huynguyen2703.songs.vibeexplorer.repositories.SongFeaturesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SongFeaturesService {
    private final SongFeaturesRepository songFeaturesRepository;

    @Autowired
    public SongFeaturesService(SongFeaturesRepository songFeaturesRepository) {
        this.songFeaturesRepository = songFeaturesRepository;
    }

    public SongFeatures saveSongFeatures(SongFeatures songFeatures) {
        return songFeaturesRepository.save(songFeatures);
    }

    public Optional<SongFeatures> getSongFeaturesById(Long id) {
        return songFeaturesRepository.findById(id);
    }

    public Optional<SongFeatures> getSongFeatureBySongId(Long songId) {
        return songFeaturesRepository.findBySongID(songId);
    }

    public List<SongFeatures> getAllSongFeatures() {
        return songFeaturesRepository.findAll();
    }

    public void deleteSongFeatures(SongFeatures songFeatures) {
        songFeaturesRepository.delete(songFeatures);
    }
}

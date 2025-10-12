package com.huynguyen2703.songs.vibeexplorer.models;

import jakarta.persistence.*;

import java.util.List;
import java.time.LocalDateTime;

@Entity
@Table(name = "song_graph")
public class SongGraph {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long songGraphID;

    private String songGraphName;

    private String algorithm;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "songGraph", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Song> songs;

    // Constructors
    public SongGraph() {
    }

    public SongGraph(
            Long songGraphID,
            String songGraphName,
            String algorithm,
            String description
    ) {
        this.songGraphID = songGraphID;
        this.songGraphName = songGraphName;
        this.algorithm = algorithm;
        this.description = description;
    }

    // Getters and Setters for JPA
    public Long getSongGraphID() {
        return songGraphID;
    }

    public void setSongGraphID(Long songGraphID) {
        this.songGraphID = songGraphID;
    }

    public String getSongGraphName() {
        return songGraphName;
    }

    public void setSongGraphName(String songGraphName) {
        this.songGraphName = songGraphName;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
}

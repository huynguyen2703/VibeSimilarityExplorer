package com.huynguyen2703.songs.vibeexplorer.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "song_cluster")
public class SongCluster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long songClusterID;

    private String songClusterName;

    private Integer clusterSize;

    private String algorithm;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "songCluster", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Song> songs;

    // Constructors
    public SongCluster () {

    };

    public SongCluster (
            Long songClusterID,
            String songClusterName,
            Integer clusterSize,
            String algorithm
    ) {
        this.songClusterID = songClusterID;
        this.songClusterName = songClusterName;
        this.clusterSize = clusterSize;
        this.algorithm = algorithm;
    };

    // Getters and Setters for JPA
    public Long getSongClusterID () {
        return songClusterID;
    }

    public void setSongClusterID (Long songClusterID) {
        this.songClusterID = songClusterID;
    }

    public String getSongClusterName () {
        return songClusterName;
    }

    public void setSongClusterName (String songClusterName) {
        this.songClusterName = songClusterName;
    }

    public Integer getClusterSize () {
        return clusterSize;
    }

    public void setClusterSize (Integer clusterSize) {
        this.clusterSize = clusterSize;
    }

    public String getAlgorithm () {
        return algorithm;
    }

    public void setAlgorithm (String algorithm) {
        this.algorithm = algorithm;
    }

    public LocalDateTime getCreatedAt () {
        return createdAt;
    }

    public void setCreatedAt (LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Song> getSongs () {
        return songs;
    }

    public void setSongs (List<Song> songs) {
        this.songs = songs;
    }
}

package com.huynguyen2703.songs.vibeexplorer.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a cluster of songs, typically generated from clustering algorithms
 * based on song features or similarities.
 * <p>
 * This entity maps to the {@code song_cluster} table in the database and
 * maintains a one-to-many relationship with {@link Song}.
 * <p>
 * Key responsibilities:
 * <ul>
 *     <li>Store metadata about the cluster such as name, size, and algorithm used</li>
 *     <li>Track creation time for auditing and analysis</li>
 *     <li>Manage the list of songs that belong to this cluster</li>
 *     <li>Support cascading operations to associated songs</li>
 * </ul>
 */
@Entity
@Table(name = "song_cluster")
public class SongCluster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long songClusterId;

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
        this.songClusterId = songClusterID;
        this.songClusterName = songClusterName;
        this.clusterSize = clusterSize;
        this.algorithm = algorithm;
    };

    // Getters and Setters for JPA
    public Long getSongClusterID () {
        return songClusterId;
    }

    public void setSongClusterID (Long songClusterID) {
        this.songClusterId = songClusterID;
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

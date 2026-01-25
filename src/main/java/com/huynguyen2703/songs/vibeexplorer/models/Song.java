package com.huynguyen2703.songs.vibeexplorer.models;

import jakarta.persistence.*;


/**
 * Represents a Song entity in the system.
 * <p>
 * This class maps to the {@code song} table in the database and stores
 * all relevant information about a song, including its Spotify ID, metadata,
 * and relationships to SongGraph, SongCluster, and SongFeatures.
 * <p>
 * Key responsibilities:
 * <ul>
 *     <li>Persist song metadata such as title, artist, album, genre, release year</li>
 *     <li>Maintain relationships with {@link SongGraph} and {@link SongCluster}</li>
 *     <li>Support cascading operations with {@link SongFeatures}</li>
 *     <li>Ensure uniqueness of Spotify ID</li>
 * </ul>
 */
@Entity
@Table(name = "song")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long songId;

    @Column(nullable = false, unique=true)
    private String spotifyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "songGraphId")
    private SongGraph songGraph;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "songClusterId")
    private SongCluster songCluster;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String artist;

    private String album;

    private String genre;

    private Integer releaseYear;

    @OneToOne(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
    private SongFeatures songFeatures;

    // Constructors
    public Song(
            String spotifyId,
            SongGraph songGraph,
            SongCluster songCluster,
            String title,
            String artist,
            String album,
            String genre,
            Integer releaseYear
    ) {
        this.spotifyId = spotifyId;
        this.songGraph = songGraph;
        this.songCluster = songCluster;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.releaseYear = releaseYear;
    }

    public Song() {

    }

    // Getters and Setters for JPA
    public Long getID() {
        return this.songId;
    }

    public void setID(Long songID) {
        this.songId = songID;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public SongGraph getSongGraph() {
        return songGraph;
    }

    public void setSongGraph(SongGraph songGraph) {
        this.songGraph = songGraph;
    }

    public SongCluster getSongCluster() {
        return songCluster;
    }

    public void setSongCluster(SongCluster songCluster) {
        this.songCluster = songCluster;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return this.album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getReleaseYear() {
        return this.releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }
}
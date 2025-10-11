package com.huynguyen2703.songs.vibeexplorer.models;

import jakarta.persistence.*;

@Entity
@Table(name = "song")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long songID;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String artist;

    private String album;

    private String genre;

    @Column(name = "release_year")
    private Integer releaseYear;

    @OneToOne(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
    private SongFeatures songFeatures;

    // Constructors
    public Song(
            Long songID,
            String title,
            String artist,
            String album,
            String genre,
            Integer releaseYear
    ) {
        this.songID = songID;
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
        return this.songID;
    }

    public void setID(Long songID) {
        this.songID = songID;
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
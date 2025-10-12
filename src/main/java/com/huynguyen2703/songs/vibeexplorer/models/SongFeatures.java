package com.huynguyen2703.songs.vibeexplorer.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "song_features")
public class SongFeatures {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long songFeatureID;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "songID", unique=true, nullable=false)
    private Song song;

    private Float danceability;

    private Float energy;

    private Integer key;

    private Float loudness;

    private Integer mode;

    private Float speechiness;

    private Float acousticness;

    private Float instrumentalness;

    private Float liveness;

    private Float valence;

    private Float tempo;

    private Float time_signature;

    private Integer duration_ms;

    private String analysis_url;

    @Column(columnDefinition = "TEXT")
    private String featureVector;

    @Transient
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Constructors
    public SongFeatures() {
    }

    public SongFeatures(
            Long songFeatureID,
            Song song,
            Float danceability,
            Float energy,
            Integer key,
            Float loudness,
            Integer mode,
            Float speechiness,
            Float acousticness,
            Float instrumentalness,
            Float liveness,
            Float valence,
            Float tempo,
            Float time_signature,
            Integer duration_ms,
            String analysis_url
    ) {
        this.songFeatureID = songFeatureID;
        this.song = song;
        this.danceability = danceability;
        this.energy = energy;
        this.key = key;
        this.loudness = loudness;
        this.mode = mode;
        this.speechiness = speechiness;
        this.acousticness = acousticness;
        this.instrumentalness = instrumentalness;
        this.liveness = liveness;
        this.valence = valence;
        this.tempo = tempo;
        this.time_signature = time_signature;
        this.duration_ms = duration_ms;
        this.analysis_url = analysis_url;
        buildFeatureVector();

    }
    public void buildFeatureVector() {
        try {
            float[] vector = new float[] {
                    danceability != null ? danceability : 0f,
                    energy != null ? energy : 0f,
                    key != null ? key.floatValue() : 0f,
                    loudness != null ? loudness : 0f,
                    mode != null ? mode.floatValue() : 0f,
                    speechiness != null ? speechiness : 0f,
                    acousticness != null ? acousticness : 0f,
                    instrumentalness != null ? instrumentalness : 0f,
                    liveness != null ? liveness : 0f,
                    valence != null ? valence : 0f,
                    tempo != null ? tempo : 0f,
                    time_signature != null ? time_signature : 0f,
                    duration_ms != null ? duration_ms.floatValue() : 0f
            };
            this.featureVector = objectMapper.writeValueAsString(vector);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing feature vector", e);
        }
    }

    @PrePersist
    @PreUpdate
    private void updateFeatureVector() {
        buildFeatureVector();
    }

    // Getters and Setters for JPA
    public Long getSongFeatureID() {
        return songFeatureID;
    }

    public void setSongFeatureID(Long songFeatureID) {
        this.songFeatureID = songFeatureID;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public Float getDanceability() { return danceability; }
    public void setDanceability(Float danceability) { this.danceability = danceability; }

    public Float getEnergy() { return energy; }
    public void setEnergy(Float energy) { this.energy = energy; }

    public Integer getKey() { return key; }
    public void setKey(Integer key) { this.key = key; }

    public Float getLoudness() { return loudness; }
    public void setLoudness(Float loudness) { this.loudness = loudness; }

    public Integer getMode() { return mode; }
    public void setMode(Integer mode) { this.mode = mode; }

    public Float getSpeechiness() { return speechiness; }
    public void setSpeechiness(Float speechiness) { this.speechiness = speechiness; }

    public Float getAcousticness() { return acousticness; }
    public void setAcousticness(Float acousticness) { this.acousticness = acousticness; }

    public Float getInstrumentalness() { return instrumentalness; }
    public void setInstrumentalness(Float instrumentalness) { this.instrumentalness = instrumentalness; }

    public Float getLiveness() { return liveness; }
    public void setLiveness(Float liveness) { this.liveness = liveness; }

    public Float getValence() { return valence; }
    public void setValence(Float valence) { this.valence = valence; }

    public Float getTempo() { return tempo; }
    public void setTempo(Float tempo) { this.tempo = tempo; }

    public Float getTime_signature() { return time_signature; }
    public void setTime_signature(Float time_signature) { this.time_signature = time_signature; }

    public Integer getDuration_ms() { return duration_ms; }
    public void setDuration_ms(Integer duration_ms) { this.duration_ms = duration_ms; }

    public String getAnalysis_url() { return analysis_url; }
    public void setAnalysis_url(String analysis_url) { this.analysis_url = analysis_url; }

    public String getFeatureVector() { return featureVector; }
    public void setFeatureVector(String featureVector) { this.featureVector = featureVector; }
}


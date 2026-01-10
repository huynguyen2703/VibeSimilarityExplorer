package com.huynguyen2703.songs.vibeexplorer.etl;

import com.huynguyen2703.songs.vibeexplorer.dto.spotify.SpotifyAlbumDto;
import com.huynguyen2703.songs.vibeexplorer.dto.spotify.SpotifyArtistDto;
import com.huynguyen2703.songs.vibeexplorer.dto.spotify.SpotifyTrackDto;
import com.huynguyen2703.songs.vibeexplorer.dto.spotify.SpotifyTracksDto;
import com.huynguyen2703.songs.vibeexplorer.models.Song;
import com.huynguyen2703.songs.vibeexplorer.models.SongCluster;
import com.huynguyen2703.songs.vibeexplorer.models.SongGraph;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/*
    Responsibility:

    Convert Spotify DTOs → Domain entities (Song, SongFeatures, etc.)

    TODOs:

    Map track name → Song.title

    Flatten artists array → Song.artist

    Extract album name & release year

    Decide genre strategy (artist-based? later)

    Handle missing / optional Spotify fields

    Ensure transformations are pure (no DB calls)
 */

@Service
public class SpotifyTransformService {
    private static final String UNKNOWN = "Unknown";
    public List<Song> transformTracks(SpotifyTracksDto spotifyTracksDto) {
        if (spotifyTracksDto == null || spotifyTracksDto.items() == null) {
            return List.of();
        }
        List<SpotifyTrackDto> spotifyTracks = spotifyTracksDto.items();
        List<Song> songs = new ArrayList<>();
        for (SpotifyTrackDto spotifyTrack : spotifyTracks) {
            String spotifyId = extractSpotifyId(spotifyTrack.id().trim());
            if (spotifyId == null) {
                System.out.println("Skipping track without Spotify ID: " + spotifyTrack.name());
                continue;
            }

            List<SpotifyArtistDto> artists = spotifyTrack.artists();
            SpotifyAlbumDto album = spotifyTrack.album();


            SongGraph songGraph = null;
            SongCluster songCluster = null;
            String songName = spotifyTrack.name().trim();
            String artistName = extractArtistName(artists);
            String albumName = extractAlbumName(album);
            String genre = null;
            Integer releaseYear = spotifyTrack.releaseYear();

            Song song = new Song(
                    spotifyId,
                    songGraph,
                    songCluster,
                    songName,
                    artistName,
                    albumName,
                    genre,
                    releaseYear
            );
            songs.add(song);
        }
        return songs;
    }

    private String extractArtistName(List<SpotifyArtistDto> artists) {
        if (artists == null || artists.isEmpty()) {
            return UNKNOWN;
        }
        return artists.get(0).name();
    }

    private String extractAlbumName(SpotifyAlbumDto album) {
        return album != null ? album.name(): UNKNOWN;
    }

    private String extractSpotifyId(String spotifyId) {
        if (spotifyId == null || spotifyId.isEmpty()) {
            return null;
        }
        return spotifyId;
    }
}

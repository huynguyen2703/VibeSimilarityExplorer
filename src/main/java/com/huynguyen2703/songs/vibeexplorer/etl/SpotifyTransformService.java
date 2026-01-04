package com.huynguyen2703.songs.vibeexplorer.etl;

import com.huynguyen2703.songs.vibeexplorer.dto.spotify.SpotifyAlbumDto;
import com.huynguyen2703.songs.vibeexplorer.dto.spotify.SpotifyArtistDto;
import com.huynguyen2703.songs.vibeexplorer.dto.spotify.SpotifyTrackDto;
import com.huynguyen2703.songs.vibeexplorer.dto.spotify.SpotifyTracksDto;
import com.huynguyen2703.songs.vibeexplorer.models.Song;
import com.huynguyen2703.songs.vibeexplorer.models.SongCluster;
import com.huynguyen2703.songs.vibeexplorer.models.SongGraph;

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


public class SpotifyTransformService {
    private static final String UNKNOWN = "Unknown";
    public List<Song> transformTracks(SpotifyTracksDto spotifyTracksDto) {
        if (spotifyTracksDto == null || spotifyTracksDto.items() == null) {
            return List.of();
        }
        List<SpotifyTrackDto> spotifyTracks = spotifyTracksDto.items();
        List<Song> songs = new ArrayList<>();
        for (SpotifyTrackDto spotifyTrack : spotifyTracks) {
            List<SpotifyArtistDto> artists = spotifyTrack.artists();
            SpotifyAlbumDto album = spotifyTrack.album();

            SongGraph songGraph = null;
            SongCluster songCluster = null;
            String songName = spotifyTrack.name();
            String artistName = extractArtistName(artists);
            String albumName = extractAlbumName(album);
            String genre = null;
            Integer releaseYear = spotifyTrack.releaseYear();

            Song song = new Song(
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
}

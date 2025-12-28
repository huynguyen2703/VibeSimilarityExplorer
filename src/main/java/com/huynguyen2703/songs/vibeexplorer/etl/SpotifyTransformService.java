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

    public List<Song> transformTracks(SpotifyTracksDto spotifyTracksDto) {
        List<SpotifyTrackDto> spotifyTracks = spotifyTracksDto.items();
        List<Song> songs = new ArrayList<>();
        for (SpotifyTrackDto spotifyTrack : spotifyTracks) {
            String songName = spotifyTrack.name();
            SpotifyAlbumDto album = spotifyTrack.album();
            Integer releaseYear = spotifyTrack.releaseYear();
            String artistName;

            List<SpotifyArtistDto> artists = spotifyTrack.artists();

            if (artists != null && !artists.isEmpty()) {
                artistName = artists.get(0).name();
            } else {
                artistName = "Unknown";
            }
            String albumName = (album != null) ? album.name() : "Unknown";

            SongGraph songGraph = null;
            SongCluster songCluster = null;
            String genre = null;

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
}

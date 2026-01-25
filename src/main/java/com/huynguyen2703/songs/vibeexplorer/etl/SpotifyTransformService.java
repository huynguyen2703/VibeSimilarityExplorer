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


/**
 * Service responsible for transforming Spotify API DTOs into domain {@link Song} entities.
 * <p>
 * Responsibilities:
 * <ul>
 *     <li>Convert {@link SpotifyTracksDto} → {@link Song}</li>
 *     <li>Flatten multiple artists into a single artist string</li>
 *     <li>Extract album information and release year</li>
 *     <li>Handle optional or missing Spotify fields gracefully</li>
 *     <li>Ensure transformation is pure (no DB calls, no side effects)</li>
 * </ul>
 * <p>
 * TODOs:
 * <ul>
 *     <li>Map track name → {@link Song#title}</li>
 *     <li>Decide genre mapping strategy (artist-based or external)</li>
 *     <li>Handle SongGraph and SongCluster assignment (later)</li>
 * </ul>
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

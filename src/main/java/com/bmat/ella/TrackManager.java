
package com.bmat.ella;

import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Java Class TrackManager.
 * Manager of Track results.
 * @author Harrington Joseph (Harph)
 * */
public class TrackManager {
    /**
     * Extract all the data from the JSONArray and create an ArrayList of
     * Track objects.
     * @param ellaConnection A connection to the Ella web service.
     * @param results The JSONArray that contains the WS response.
     * @return An ArrayList of Track objects created from the JSONArray data.
     * */
    public final ArrayList<Track> getTracks(final EllaConnection ellaConnection,
            final JSONArray results) {
        return this.getTracks(ellaConnection, results, null);
    }

    /**
     * Extract all the data from the JSONArray and create an ArrayList of
     * Track objects.
     * @param ellaConnection A connection to the Ella web service.
     * @param results The JSONArray that contains the WS response.
     * @param threshold The lowest Track score to consider. In case of null,
     * it will be considered as 0.
     * @return An ArrayList of Track objects created from the JSONArray data.
     * */
    public final ArrayList<Track> getTracks(final EllaConnection ellaConnection,
            final JSONArray results, final Double threshold) {
        ArrayList<Track> tracks = new ArrayList<Track>();
        for (Object json : results) {
            JSONObject jsonTrack = (JSONObject) json;
            JSONObject jsonEntity = (JSONObject) jsonTrack.get("entity");
            JSONObject jsonMetadata = (JSONObject) jsonEntity.get("metadata");
            String trackId = (String) jsonEntity.get("id");
            if (trackId == null || trackId.trim().equals("")) {
                continue;
            }
            String entityCollection = (String) jsonEntity.get("collection");

            Object relevance = jsonTrack.get("score");
            double score = 0.0;
            if (relevance != null && !relevance.toString().equals("")) {
                score = new Double(relevance.toString());
            }

            if (threshold != null && score < threshold) {
                break;
            }

            String artistId = (String) jsonMetadata.get("artist_service_id");
            if (artistId == null) {
                continue;
            }

            Artist artist = new Artist(ellaConnection, artistId,
                    entityCollection);
            artist.setName((String) jsonMetadata.get("artist"));
            if (artist.getName() == null) {
                continue;
            }
            Object apop = jsonEntity.get("artist_popularity");
            double artistPopularity = 0.0;
            if (apop != null && !apop.toString().equals("")) {
                artistPopularity = new Double(apop.toString());
            }
            artist.setPopularity(artistPopularity);

            Object artistLocation = jsonMetadata.get("artist_location");
            if (artistLocation != null && !artistLocation.toString()
                    .equals("")) {
                artist.setLocation(artistLocation.toString());
            } else {
                artist.setLocation("");
            }
            Object recommend = jsonMetadata.get("recommendable");
            artist.setRecommend(recommend);

            Track track = new Track(ellaConnection, trackId, entityCollection);
            track.setTitle((String) jsonMetadata.get("track"));
            track.setAudio((String) jsonMetadata.get("location"));
            track.setArtist(artist);
            track.setArtistName(artist.getName());
            track.setArtistId(artist.getId());
            track.setRecommend(recommend);

            Object tpop = jsonMetadata.get("track_popularity");
            double trackPopularity = 0.0;
            if (tpop != null && !tpop.toString().equals("")) {
                trackPopularity = new Double(tpop.toString());
            }
            track.setPopularity(trackPopularity);

            Object trackSmallImages = jsonMetadata.get("track_small_image");
            if (trackSmallImages instanceof String) {
                track.setImages((String) trackSmallImages);
            } else if (trackSmallImages != null) {
                track.setImages((JSONArray) trackSmallImages);
            }

            Object albumId = jsonMetadata.get("release_service_id");
            if (albumId != null) {
                Album album = new Album(ellaConnection, (String) albumId,
                        entityCollection);
                album.setTitle((String) jsonMetadata.get("release"));
                album.setArtist(artist);
                album.setImage((String) jsonMetadata.get(
                        "release_small_image"));
                track.setAlbum(album);
                track.setAlbumTitle(album.getTitle());
                track.setAlbumId(album.getId());
            }
            tracks.add(track);
        }
        return tracks;
    }
}

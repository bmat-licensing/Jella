
package com.bmat.ella;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Java Class Track.
 * Represents a BMAT Track.
 * @author Harrington Joseph (Harph)
 * */
public class Track extends BaseObject implements Comparable<Track> {
    /**
     * Track title.
     * */
    private String title;
    /**
     * Artist instance associated with the track.
     * */
    private Artist artist;
    /**
     * ID of the artist associated with the track.
     * */
    private String artistId;
    /**
     * Name of the artist associated with the track.
     * */
    private String artistName;
    /**
     * Album instance associated with the track.
     * */
    private Album album;
    /**
     * ID of the album associated with the track.
     * */
    private String albumId;
    /**
     * Title of the artist associated with the track.
     * */
    private String albumTitle;
    /**
     * URL to the track audio file.
     * */
    private String audio;
    /**
     * ArrayList of String that represents the images of the track.
     * */
    private ArrayList<String> images;
    /**
     * Track popularity.
     * */
    private Double popularity;

    /**
     * Class constructor.
     * @param ellaConnection A connection to the Ella web service.
     * @param id The id of the song.
     * @param collection The collection name of the album.
     * */
    public Track(final EllaConnection ellaConnection, final String id,
            final String collection) {
        super(ellaConnection, id, collection);
        this.method = "/tracks/" + this.id + SearchObject.RESPONSE_TYPE;
        this.metadataLinks = new String[]{"spotify_track_url",
                "grooveshark_track_url", "amazon_track_url",
                "itms_track_url", "hypem_track_url",
                "musicbrainz_track_url"};
        this.metadata = "track,name,artist_service_id,artist,"
            + "release_service_id,release,location,year,genre,"
            + "track_popularity,track_small_image,recommendable,"
            + "spotify_track_uri,";
        this.metadata += Util.joinArray(this.metadataLinks, ",");
    }

    /**
     * @return A String like "Artist name - Track title".
     * */
    public final String getFullTitle() {
        return this.getArtistName() + " - " + this.getTitle();
    }

    /**
     * @return The title of the track.
     * */
    public final String getTitle() {
        if (this.title == null) {
            this.title = this.getFieldValue("name");
        }
        if (this.title.equals("")) {
            this.title = this.getFieldValue("track");
        }
        return title;
    }

    /**
     * Sets The title of the track.
     * @param titleValue The title of the track.
     * */
    public final void setTitle(final String titleValue) {
        this.title = titleValue;
    }

    /**
     * @return The associated Artist instance.
     * */
    public final Artist getArtist() {
        if (this.artist == null) {
            String metaid = this.getFieldValue("artist_service_id");
            if (!metaid.equals("")) {
                this.artistId = metaid;
                this.artist = new Artist(this.request.getEllaConnection(),
                        this.artistId, this.collection);
            }
        }
        return this.artist;
    }

    /**
     * Sets the Artist of the track.
     * @param artistValue The Artist of the track.
     * */
    public final void setArtist(final Artist artistValue) {
        this.artist = artistValue;
    }

    /**
     * @return The artist id of the track.
     * */
    public final String getArtistId() {
        if (this.artistId == null) {
            String metaid = this.getFieldValue("artist_service_id");
            if (!metaid.equals("")) {
                this.artistId = metaid;
            }
        }
        return this.artistId;
    }

    /**
     * Sets The artist id of the track.
     * @param artistIdValue The artist id of the track.
     * */
    public final void setArtistId(final String artistIdValue) {
        this.artistId = artistIdValue;
    }

    /**
     * @return The artist name of the track.
     * */
    public final String getArtistName() {
        if (this.artistName == null) {
            this.artistName = this.getFieldValue("artist");
        }
        return this.artistName;
    }

    /**
     * Sets the artist name of the track.
     * @param artistNameValue The artist name of the track.
     * */
    public final void setArtistName(final String artistNameValue) {
        this.artistName = artistNameValue;
    }

    /**
     * @return The associated Album instance.
     * */
    public final Album getAlbum() {
        if (album == null) {
            String metaid = this.getFieldValue("release_service_id");
            if (metaid != null) {
                this.albumId = metaid;
                this.album = new Album(this.request.getEllaConnection(),
                        this.albumId, this.collection);
            }
        }
        return this.album;
    }

    /**
     * Sets the Album of the track.
     * @param albumValue The Album of the track.
     * */
    public final void setAlbum(final Album albumValue) {
        this.album = albumValue;
    }

    /**
     * @return The album id of the track.
     * */
    public final String getAlbumId() {
        if (this.albumId == null) {
            this.albumId = this.getFieldValue("release_service_id");
        }
        return this.albumId;
    }

    /**
     * Sets the album id of the track.
     * @param albumIdValue The album id of the track.
     * */
    public final void setAlbumId(final String albumIdValue) {
        this.albumId = albumIdValue;
    }

    /**
     * @return The album title of the track.
     * */
    public final String getAlbumTitle() {
        if (this.albumTitle == null) {
            this.albumTitle = this.getFieldValue("release");
        }
        return this.albumTitle;
    }

    /**
     * Sets The album title of the track.
     * @param albumTitleValue The album title of the track.
     * */
    public final void setAlbumTitle(final String albumTitleValue) {
        this.albumTitle = albumTitleValue;
    }

    /**
     * @return The audio URL of the track.
     * */
    public final String getAudio() {
        if (this.audio == null) {
            this.audio = this.getFieldValue("location");
        }
        return audio;
    }

    /**
     * Sets The audio URL of the track.
     * @param audioValue The audio URL of the track.
     * */
    public final void setAudio(final String audioValue) {
        this.audio = audioValue;
    }

    /**
     * @return an ArrayList of String with the images of the track.
     * */
    public final ArrayList<String> getImages() {
        if (this.images == null) {
            JSONArray meta = this.getFieldValues("track_small_image");
            if (meta != null) {
                this.setImages(meta);
            }
        }
        return images;
    }

    /**
     * Sets the album title of the track.
     * @param imageValue The images of the track.
     * */
    public final void setImages(final String imageValue) {
        if (this.images == null) {
            this.images = new ArrayList<String>();
        }
        this.images.add(imageValue);
    }

    /**
     * Sets the images of the track.
     * @param imageValues A JSONArray with the images of the track.
     * */
    public final void setImages(final JSONArray imageValues) {
        for (int i = 0; i < imageValues.size(); i++) {
            this.setImages(imageValues.get(i).toString());
        }
    }

    /**
     * @return The mbid of the track.
     * */
    public final String getMbid() {
        if (this.mbid == null) {
            this.setMbid(this.getFieldValue("musicbrainz_track_id"));
        }
        return this.mbid;
    }

    /**
     * @return The popularity of the track.
     * */
    public final double getPopularity() {
        if (this.popularity == null) {
            String meta = this.getFieldValue("track_popularity");
            if (meta != null) {
                this.popularity = new Double(meta);
            } else {
                this.popularity = new Double(0);
            }
        }
        return this.popularity;
    }

    /**
     * Sets the popularity of the track.
     * @param popularityValue The popularity of the track.
     * */
    public final void setPopularity(final double popularityValue) {
        this.popularity = popularityValue;
    }

    /**
     * @return An ArrayList with similar artists.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    public final ArrayList<Track> getSimilarTracks()
    throws ServiceException, IOException {
        return this.getSimilarTracks(Jella.getDEFAULT_LIMIT(),
                null, null, null, null, null);
    }

    /**
     * Searches for similar tracks.
     * @param limit Max number of results.
     * @param filter Search filters.
     * @param collectionSim Name of the collection it will look for similar.
     * @param seeds Similarities to fulfill.
     * @param threshold Score tolerance.
     * @param similarityType The name of the similarity strategy to use.
     * @return An ArrayList with similar tracks.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    public final ArrayList<Track> getSimilarTracks(final long limit,
            final String[] filter,
            final String collectionSim, final HashMap<String,
            HashMap<String, String>> seeds, final Double threshold,
            final String similarityType) throws ServiceException, IOException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("limit", Long.toString(limit));
        params.put("fetch_metadata", this.metadata);

        if (filter != null) {
            params.put("filter", Util.joinArray(filter, " AND "));
        }

        if (this.id != null) {
            params.put("seeds", this.collection + ":track/" + this.id);
        }

        if (seeds != null) {
            for (String key : seeds.keySet()) {
                if (params.containsKey("seeds")) {
                    params.put("seeds", params.get("seeds") + ","
                            + seeds.get(key).get("collection") + ":"
                            + seeds.get(key).get("entity") + "/" + key);
                } else {
                    params.put("seeds", seeds.get(key).get("collection")
                            + ":" + seeds.get(key).get("entity") + "/" + key);
                }
            }
        }

        if (similarityType != null) {
            params.put("similarity_type", similarityType);
        }

        String collectionRef = this.collection;
        if (collectionSim == null) {
            collectionRef = collectionSim;
        }
        String mtd = "/tracks/similar_to" + this.RESPONSE_TYPE;

        if (!this.isRecommend()) {
            return null;
        }
        JSONObject response = (JSONObject) this.request(mtd, collectionRef,
                params);
        JSONArray results = (JSONArray) response.get("results");
        ArrayList<Track> similars = new TrackManager().getTracks(
                this.request.getEllaConnection(), results, threshold);
        return similars;
    }

    /**
     * Compares this one album to another.
     * @param object A Track instance to be compared.
     * @return The value of the string comparation between IDs.
     * */
    public final int compareTo(final Track object) {
        return this.id.compareTo(object.id);
    }

    /**
     * Checks if the IDs are the same.
     * @param object A Track instance to be compared.
     * @return The value of the string equals comparation between IDs.
     * */
    public final boolean equals(final Track object) {
        return this.id.equals(object.id);
    }
}

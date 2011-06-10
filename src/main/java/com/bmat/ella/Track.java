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
public class Track extends BaseObject{
    private String title;
    private Artist artist;
    private String artistId;
    private String artistName;
    private Album album;
    private String albumId;
    private String albumTitle;
    private String audio;
    private ArrayList<String> images;
    private Double popularity;
    //    private String tags;

    /**
     * Class constructor.
     * @param ellaConnection a connection to the Ella web service. 
     * @param id the id of the song
     * @param collection the collection name of the album.
     * */
    public Track(EllaConnection ellaConnection, String id, String collection) {
        super(ellaConnection, id, collection);
        this.method = "/tracks/" + this.id + ".json";
        this.metadataLinks = new String[]{"spotify_track_url", "grooveshark_track_url", "amazon_track_url","itms_track_url","hypem_track_url","musicbrainz_track_url"};
        this.metadata = "track,name,artist_service_id,artist,release_service_id,release,location,year,genre,track_popularity,track_small_image,recommendable,spotify_track_uri,";
        this.metadata += Util.joinArray(this.metadataLinks, ",");
    }

    /**
     * @return a String like "Artist name - Track title".
     * */
    public final String getFullTitle() {
        return this.getArtistName() + " - " + this.getTitle();
    }

    /**
     * @return the title of the track.
     * */
    public final String getTitle() {
        if (this.title == null)
            this.title = this.getFieldValue("name");
        this.title = this.title.equals("") ? this.getFieldValue("track") : this.title;
        return title;
    }

    /**
     * Sets the title of the track.
     * @param title the title of the track. 
     * */
    public final void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the associated Artist instance
     * */
    public Artist getArtist() {
        if (this.artist == null) {
            String metaid = this.getFieldValue("artist_service_id");
            if (!metaid.equals("")) {
                this.artistId = metaid;
                this.artist = new Artist(this.request.getEllaConnection(), this.artistId, this.collection);
            }
        }
        return this.artist;
    }

    /**
     * Sets the Artist of the track.
     * @param artist the Artist of the track. 
     * */
    public final void setArtist(Artist artist) {
        this.artist = artist;
    }

    /**
     * @return the artist id of the track.
     * */
    public final String getArtistId() {
        if (this.artistId == null) {
            String metaid = this.getFieldValue("artist_service_id");
            if (!metaid.equals(""))
                this.artistId = metaid;
        }
        return this.artistId;
    }

    /**
     * Sets the artist id of the track.
     * @param artistId the artist id of the track. 
     * */
    public final void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    /**
     * @return the artist name of the track.
     * */
    public final String getArtistName() {
        if (this.artistName == null)
            this.artistName = this.getFieldValue("artist");
        return this.artistName;
    }

    /**
     * Sets the artist name of the track.
     * @param artistName the artist name of the track. 
     * */
    public final void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    /**
     * @return the associated Album instance
     * */
    public final Album getAlbum() {
        if (album == null) {
            String metaid = this.getFieldValue("release_service_id");
            if (metaid != null) {
                this.albumId = metaid;
                this.album = new Album(this.request.getEllaConnection(), this.albumId, this.collection);
            }
        }
        return this.album;
    }

    /**
     * Sets the Album of the track.
     * @param album the Album of the track. 
     * */
    public final void setAlbum(Album album) {
        this.album = album;
    }

    /**
     * @return the album id of the track.
     * */
    public final String getAlbumId() {
        if (this.albumId == null)
            this.albumId = this.getFieldValue("release_service_id");
        return this.albumId;
    }

    /**
     * Sets the album id of the track.
     * @param albumId the album id of the track. 
     * */
    public final void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    /**
     * @return the album title of the track.
     * */
    public final String getAlbumTitle() {
        if (this.albumTitle == null)
            this.albumTitle = this.getFieldValue("release");
        return this.albumTitle;
    }

    /**
     * Sets the album title of the track.
     * @param albumTitle the album title of the track. 
     * */
    public final void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    /**
     * @return the audio URL of the track.
     * */
    public final String getAudio() {
        if (this.audio == null)
            this.audio = this.getFieldValue("location");
        return audio;
    }

    /**
     * Sets the audio URL of the track.
     * @param audio the audio URL of the track. 
     * */
    public final void setAudio(String audio) {
        this.audio = audio;
    }

    /**
     * @return an ArrayList of String with the images of the track.
     * */
    public final ArrayList<String> getImages() {
        if (this.images == null) {
            JSONArray meta = this.getFieldValues("track_small_image");
            if (meta != null)
                this.setImages(meta);
        }
        return images;
    }

    /**
     * Sets the album title of the track.
     * @param albumTitle the album title of the track. 
     * */
    public final void setImages(String image) {
        if (this.images == null)
            this.images = new ArrayList<String>();
        this.images.add(image);
    }

    /**
     * Sets the images of the track.
     * @param images a JSONArray with the images of the track. 
     * */
    public final void setImages(JSONArray images) {
        for (int i = 0; i<images.size(); i++)
            this.setImages(images.get(i).toString());
    }

    /**
     * @return the mbid of the track.
     * */
    public final String getMbid() {
        if (this.mbid == null)
            this.setMbid(this.getFieldValue("musicbrainz_track_id"));
        return this.mbid;
    }

    /**
     * @return the popularity of the track.
     * */
    public final double getPopularity() {
        if (this.popularity == null) {
            String meta = this.getFieldValue("track_popularity");
            if (meta != null)
                this.popularity = new Double(meta);
            else
                this.popularity = new Double(0);
        }
        return this.popularity;
    }

    /**
     * Sets the popularity of the track.
     * @param popularity the popularity of the track. 
     * */
    public final void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    /**
     * @return an ArrayList with similar artists.
     * */
    public final ArrayList<Track> getSimilarTracks() throws ServiceException, IOException{
        return this.getSimilarTracks(20, null, null, null, null, null);
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
     * */
    public final ArrayList<Track> getSimilarTracks(long limit, String[] filter, String collectionSim, HashMap<String, HashMap<String, String>> seeds, Double threshold, String similarityType) throws ServiceException, IOException{
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
            for (String key:seeds.keySet()) {
                if (params.containsKey("seeds")) {
                    params.put("seeds", params.get("seeds") + "," + seeds.get(key).get("collection") + ":" + seeds.get(key).get("entity") + "/" + key);
                }
                else {
                    params.put("seeds", seeds.get(key).get("collection") + ":" + seeds.get(key).get("entity") + "/" + key);
                }
            }
        }

        if (similarityType != null)
            params.put("similarity_type", similarityType);

        if (collectionSim == null)
            collectionSim = this.collection;

        String mtd = "/tracks/similar_to" + this.RESPONSE_TYPE;

        if (!this.isRecommend())
            return null;

        JSONObject response = (JSONObject)this.request(mtd, collectionSim,
                params);
        JSONArray results = (JSONArray) response.get("results");
        ArrayList<Track> similars = new TrackManager().getTracks(
                this.request.getEllaConnection(), results, threshold);
        return similars;
    }
}

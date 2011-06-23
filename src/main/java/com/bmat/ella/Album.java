
package com.bmat.ella;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Java Class Album.
 * Represents a BMAT Album.
 * @author Harrington Joseph (Harph)
 * */
public class Album extends BaseObject implements Comparable<Album> {
    /**
     * Title of the album.
     * */
    private String title;
    /**
     * Artist instance associated with the album.
     * */
    private Artist artist;
    /**
     * ArrayList of String that represents the images of the album.
     * */
    private ArrayList<String> images;
    /**
     * ArrayList of String that represents the labels of the album.
     * */
    private ArrayList<String> labels;
    /**
     * ArrayList of Track instances associated with the album.
     * */
    private ArrayList<Track> tracks;

    /**
     * Class constructor.
     * @param ellaConnection A connection to the Ella web service.
     * @param id The id of the album.
     * @param collection The collection name of the album.
     * */
    public Album(final EllaConnection ellaConnection,
            final String id, final String collection) {
        super(ellaConnection, id, collection);
        this.method = "/releases/" + this.id + SearchObject.RESPONSE_TYPE;
        this.metadataLinks = new String[]{"spotify_release_url",
                "amazon_release_url", "itms_release_url",
                "rhapsody_release_url", "emusic_release_url",
                "limewire_release_url", "trackitdown_release_url",
                "juno_release_url", "rateyourmusic_release_url",
                "metacritic_release_url", "pitchfork_release_url",
                "bbc_co_uk_release_url", "rollingstone_release_url",
                "cloudspeakers_url"};
        this.metadata = "release,name,artist_service_id,release_small_image,"
            + "release_label,musicbrainz_release_id";
        this.metadata += Util.joinArray(this.metadataLinks, ",");
    }

    /**
     * @return The title of the album.
     */
    public final String getTitle() {
        if (this.title == null) {
            this.title = this.getFieldValue("release");
        }
        return this.title;
    }

    /**
     * Sets the title of the album.
     * @param titleValue A string with the title value.
     * */
    public final void setTitle(final String titleValue) {
        this.title = titleValue;
    }

    /**
     * @return The associated Artist instance.
     */
    public final Artist getArtist() {
        if (this.artist == null) {
            String artistId = this.getFieldValue("artist_service_id");
            if (artistId != null) {
                this.artist = new Artist(this.request.getEllaConnection(),
                        artistId, this.collection);
            }
        }
        return this.artist;
    }

    /**
     * Sets the Artist of the album.
     * @param artistValue The Artist associated instance.
     * */
    public final void setArtist(final Artist artistValue) {
        this.artist = artistValue;
    }

    /**
     * @return The images of the album.
     * */
    public final ArrayList<String> getImages() {
        if (this.images == null) {
            try {
                JSONArray meta = this.getFieldValues("release_small_image");
                if (meta != null) {
                    for (int i = 0; i < meta.size(); i++) {
                        this.setImage(meta.toJSONString());
                    }
                }
            } catch (Exception e) {
                String meta = this.getFieldValue("release_small_image");
                if (meta != null) {
                    this.setImage(meta);
                }
            }
        }
        return images;
    }

    /**
     * Add a String to the images list.
     * @param imageValue A String with a image of the album.
     * */
    public final void setImage(final String imageValue) {
        if (this.images == null) {
            this.images = new ArrayList<String>();
        }
        this.images.add(imageValue);
    }

    /**
     * Sets the images of the album.
     * @param imageValues An ArrayList of String with the images of the album.
     * */
    public final void setImages(final ArrayList<String> imageValues) {
        this.images = imageValues;
    }

    /**
     * @return The mbid of the album.
     * */
    public final String getMbid() {
        if (this.mbid == null) {
            this.setMbid(this.getFieldValue("musicbrainz_release_id"));
        }
        return this.mbid;
    }

    /**
     * @return The labels of the album.
     * */
    public final ArrayList<String> getLabels() {
        if (this.labels == null) {
            try {
                JSONArray meta = this.getFieldValues("release_label");
                if (meta != null) {
                    for (int i = 0; i < meta.size(); i++) {
                        this.setLabel(meta.get(i).toString());
                    }
                }
            } catch (Exception e) {
                String meta = this.getFieldValue("release_label");
                if (meta != null) {
                    this.setLabel(meta);
                }
            }
        }
        return this.labels;
    }

    /**
     * Add a String to the labels list.
     * @param labelValue A String with a label of the album.
     * */
    public final void setLabel(final String labelValue) {
        if (this.labels == null) {
            this.labels = new ArrayList<String>();
        }
        this.labels.add(labelValue);
    }

    /**
     * Sets the labels of the album.
     * @param labelValues An ArrayList of String with the labels of the album.
     * */
    public final void setLabels(final ArrayList<String> labelValues) {
        this.labels = labelValues;
    }

    /**
     * @return An ArrayList with the tracks of the album.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    public final ArrayList<Track> getTracks()
    throws ServiceException, IOException {
        if (this.tracks != null) {
            return this.tracks;
        }

        this.tracks = new ArrayList<Track>();
        String mtd = "/releases/" + this.id + "/tracks.json";
        String trackMetadata = "track,artist_service_id,artist,"
            + "release_service_id,release,location,year,genre,"
            + "track_popularity,track_small_image,recommendable,"
            + "artist_decades1,artist_decades2,musicbrainz_track_id,";
        String[] trackMetadataLinks = new String[]{"spotify_track_url",
                "grooveshark_track_url", "amazon_track_url", "itms_track_url",
                "hypem_track_url", "musicbrainz_track_url"};
        trackMetadata += Util.joinArray(trackMetadataLinks, ",");
        HashMap<String, String> fetchMetadata = new HashMap<String, String>();
        fetchMetadata.put("fetch_metadata", trackMetadata);
        JSONObject response = (JSONObject) this.request(mtd, fetchMetadata);
        JSONArray results = (JSONArray) response.get("results");
        this.tracks = new TrackManager().getTracks(
                this.request.getEllaConnection(), results, this);
        return this.tracks;
    }

    /**
     * Compares this one album to another.
     * @param object An Album instance to be compared.
     * @return The value of the string comparation between IDs.
     * */
    public final int compareTo(final Album object) {
        return this.id.compareTo(object.id);
    }

    /**
     * Checks if the IDs are the same.
     * @param object An Album instance to be compared.
     * @return The value of the string equals comparation between IDs.
     * */
    public final boolean equals(final Album object) {
        return this.id.equals(object.id);
    }
}

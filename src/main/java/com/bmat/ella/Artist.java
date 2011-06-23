
package com.bmat.ella;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Java Class Artist.
 * Represents a BMAT Artist.
 * @author Harrington Joseph (Harph)
 * */
public class Artist extends BaseObject implements Comparable<Artist> {
    /**
     * Artist name.
     * */
    private String name;
    /**
     * Artist location.
     * */
    private String location;
    /**
     * Artist latitude.
     * */
    private Double lat;
    /**
     * Artist longitude.
     * */
    private Double lng;
    /**
     * Artist latitudes and longitudes.
     * */
    private ArrayList<HashMap<String, Double>> latlng;
    /**
     * Artist popularity.
     * */
    private Double popularity;
    /**
     * Artist decades.
     * */
    private String[] decades;
    /**
     * Similar artist.
     * */
    private ArrayList<Object[]> similarArtists;
    /**
     * Artist tracks.
     * */
    private ArrayList<Track> tracks;
    /**
     * Related tags. Each item is an Objects[2] which contains in the tag
     * score (Double) in position '0' and tag ID (String) in position '1'.
     * */
    private ArrayList<Object[]> tags;
    /**
     * Default max number of tag results.
     * */
    private final int DEFAULT_TAG_LIMIT = 4;
    /**
     * Default tag type.
     * */
    private final String DEFAULT_TAG_TYPE = "style";
    /**
     * Default tag weight.
     * */
    private final double DEFAULT_TAG_WEIGHT = 0.70;


    /**
     * Class constructor.
     * @param ellaConnection A connection to the Ella web service.
     * @param id Artist id.
     * @param collection The collection name of the artist.
     * */
    public Artist(final EllaConnection ellaConnection, final String id,
            final String collection) {
        super(ellaConnection, id, collection);
        this.method = "/artists/" + this.id + this.RESPONSE_TYPE;
        this.metadataLinks = new String[]{"official_homepage_artist_url",
                "wikipedia_artist_url", "lastfm_artist_url",
                "myspace_artist_url", "spotify_artist_url", "itms_artist_url",
        "discogs_artist_url"};
        this.metadata = "artist,name,artist_popularity,artist_location,"
            + "recommendable,artist_decades1,artist_decades2,artist_latlng,"
            + "musicbrainz_artist_id,";
        this.metadata += Util.joinArray(this.metadataLinks, ",");
    }

    /**
     * @return The artist name.
     */
    public final String getName() {
        if (name == null) {
            this.name = this.getFieldValue("name");
        }
        return this.name;
    }

    /**
     * Sets the artist name.
     * @param nameValue The artist name.
     * */
    public final void setName(final String nameValue) {
        this.name = nameValue;
    }

    /**
     * @return The artist location.
     */
    public final String getLocation() {
        if (this.location == null) {
            this.location = this.getFieldValue("artist_location");
        }
        return location;
    }

    /**
     * Sets the artist location.
     * @param locationValue The artist location.
     * */
    public final  void setLocation(final String locationValue) {
        this.location = locationValue;
    }

    /**
     * @return An ArrayList of latitudes and longitudes of the artist.
     */
    public final ArrayList<HashMap<String, Double>> getLatlng() {
        if (this.latlng == null) {
            try {
                JSONArray latlngValues = this.getFieldValues("artist_latlng");
                if (latlngValues != null) {
                    this.setLatlng(latlngValues);
                }
            } catch (ClassCastException e) {
                String latlngValues = this.getFieldValue("artist_latlng");
                if (latlngValues != null) {
                    this.setLatlng(latlngValues);
                }
            }
        }
        return latlng;
    }

    /**
     * Adds a set of latitudes and longitudes.
     * @param latlngValue A String with latitudes and longitudes.
     * */
    public final void setLatlng(final String latlngValue) {
        if (this.latlng == null) {
            this.latlng = new ArrayList<HashMap<String, Double>>();
        }

        if (!latlngValue.equals("")) {
            String[] latLngArray = latlngValue.split(",");
            HashMap<String, Double> map = new HashMap<String, Double>();
            map.put("lat", new Double(latLngArray[0]));
            map.put("lng", new Double(latLngArray[1]));
            this.latlng.add(map);
        }
    }

    /**
     * Sets the artist latitudes and longitudes.
     * @param latlngsValue A JSONArray with latitudes and longitudes.
     * */
    public final void setLatlng(final JSONArray latlngsValue) {
        for (int i = 0; i < latlngsValue.size(); i++) {
            this.setLatlng(latlngsValue.get(i).toString());
        }
    }

    /**
     * @return The artist mbid.
     * */
    public final String getMbid() {
        if (this.mbid == null) {
            this.setMbid(this.getFieldValue("musicbrainz_artist_id"));
        }
        return this.mbid;
    }

    /**
     * @return The artist popularity.
     * */
    public final double getPopularity() {
        if (this.popularity == null) {
            String meta = this.getFieldValue("artist_popularity");
            if (meta != null) {
                this.popularity = new Double(meta);
            } else {
                this.popularity = new Double(0);
            }
        }
        return this.popularity;
    }

    /**
     * Sets the artist popularity.
     * @param popularityValue The artist popularity.
     * */
    public final void setPopularity(final double popularityValue) {
        this.popularity = popularityValue;
    }

    /**
     * @return A Array of Strings with the artist decades.
     * */
    public final String[] getDecades() {
        if (this.decades == null) {
            this.setDecades(this.getFieldValue("artist_decades1"),
                    this.getFieldValue("artist_decades2"));
        }
        return this.decades;
    }

    /**
     * Sets the artist decades.
     * @param decade1Value The first decade.
     * @param decade2Value The last decade.
     * */
    public final void setDecades(final String decade1Value,
            final String decade2Value) {
        if (this.decades == null) {
            this.decades = new String[2];
        }
        if (decade1Value != null) {
            this.decades[0] = decade1Value;
        }
        if (decade2Value != null) {
            this.decades[1] = decade2Value;
        }
    }

    /**
     * @return an ArrayList with the artist tracks.
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
        String mtd = "/artists/" + this.id + "/tracks" + this.RESPONSE_TYPE;
        String trackMetadata = "track,artist_service_id,artist,"
            + "release_service_id,release,location,year,genre,"
            + "track_popularity,track_small_image,recommendable,"
            + "musicbrainz_track_id,spotify_track_uri,";
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
     * @return an ArrayList with similar artists and scores.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    public final ArrayList<Object[]> getSimilarArtists()
    throws ServiceException, IOException {
        if (!this.isRecommend()) {
            return null;
        } else if (this.similarArtists != null) {
            return this.similarArtists;
        }
        this.similarArtists = new ArrayList<Object[]>();
        String mtd = "/artists/" + this.id + "/similar/artists"
        + this.RESPONSE_TYPE;
        HashMap<String, String> fetchMetadata = new HashMap<String, String>();
        fetchMetadata.put("fetch_metadata", this.metadata);
        JSONObject response = (JSONObject) this.request(mtd, fetchMetadata);
        JSONArray results = (JSONArray) response.get("results");
        for (Object json : results) {
            JSONObject jsonArtist = (JSONObject) json;
            JSONObject jsonEntity = (JSONObject) jsonArtist.get("entity");
            JSONObject jsonMetadata = (JSONObject) jsonEntity.get("metadata");
            String artistId = (String) jsonEntity.get("id");
            if (artistId == null || artistId.trim().equals("")) {
                continue;
            }
            Artist artist = new Artist(this.request.getEllaConnection(),
                    artistId, this.collection);
            artist.setName((String) jsonMetadata.get("name"));

            Object apop = jsonMetadata.get("artist_popularity");
            double artistPopularity = 0.0;
            if (apop != null && !apop.toString().equals("")) {
                artistPopularity = new Double(apop.toString());
            }
            artist.setPopularity(artistPopularity);
            String artistLocation = (String) jsonMetadata.get(
            "artist_location");
            if (artistLocation != null) {
                artist.setLocation(artistLocation);
            } else {
                artist.setLocation("");
            }
            Object recommend = jsonMetadata.get("recommendable");
            artist.setRecommend(recommend);
            Object score = jsonArtist.get("score");
            if (score == null || score.equals("")) {
                score = "0";
            }
            this.similarArtists.add(new Object[]{artist,
                    new Double(score.toString())});
        }
        return this.similarArtists;
    }

    /**
     * @return The artist latitude.
     * */
    public final Double getLat() {
        if (this.lat == null) {
            String latValue = this.getFieldValue("artist_lat");
            if (latValue != null) {
                this.lat = new Double(latValue);
            }
        }
        return this.lat;
    }

    /**
     * Sets the artist latitude.
     * @param latValue The artist's latitude.
     * */
    public final void setLat(final Double latValue) {
        this.lat = latValue;
    }

    /**
     * @return The artist's longitude.
     * */
    public final Double getLng() {
        if (this.lng == null) {
            String lngValue = this.getFieldValue("artist_lng");
            if (lngValue != null) {
                this.lng = new Double(lngValue);
            }
        }
        return this.lng;
    }

    /**
     * Sets the artist longitude.
     * @param lngValue The artist longitude.
     * */
    public final void setLng(final Double lngValue) {
        this.lng = lngValue;
    }

    /**
     * @return the tag cloud of the artist using tagType = DEFAULT_TAG_TYPE
     * tagWeight = DEFAULT_TAG_WEIGHT and limit = DEFAULT_TAG_LIMIT.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    public final ArrayList<Object[]> getTags()
    throws ServiceException, IOException {
        return this.getTags(this.DEFAULT_TAG_TYPE,
                this.DEFAULT_TAG_WEIGHT, this.DEFAULT_TAG_LIMIT);
    }

    /**
     * @param tagType The kind of the tags.
     * @param tagWeight The lowest tag weight allowed.
     * @param limit Max number of that tags to find.
     * @return the tag cloud of the artist.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    public final ArrayList<Object[]> getTags(final String tagType,
            final double tagWeight, final int limit)
            throws ServiceException, IOException {
        if (!this.isRecommend()) {
            return null;
        }
        if (this.tags == null) {
            this.tags = new ArrayList<Object[]>();
            String mtd = "/artists/" + this.id
            + "/similar/collections/tags/tags" + this.RESPONSE_TYPE;
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("limit", Integer.toString(limit));
            params.put("filter", "tag_type:" + tagType);
            JSONObject response = (JSONObject) this.request(mtd, params);
            JSONArray results = (JSONArray) response.get("results");
            this.tags = new TagManager().getTags(
                    this.request.getEllaConnection(), results, tagWeight);
        }
        return this.tags;
    }

    /**
     * Compares this one album to another.
     * @param object An Artist instance to be compared.
     * @return The value of the string comparation between IDs.
     * */
    public final int compareTo(final Artist object) {
        return this.id.compareTo(object.id);
    }

    /**
     * Checks if the IDs are the same.
     * @param object An Artist instance to be compared.
     * @return The value of the string equals comparation between IDs.
     * */
    public final boolean equals(final Artist object) {
        return this.id.equals(object.id);
    }
}

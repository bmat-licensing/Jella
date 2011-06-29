
package com.bmat.ella;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Java Class Tag.
 * Represents a BMAT Tag.
 * @author Harrington Joseph (Harph)
 * */
public class Tag extends BaseObject implements Comparable<Tag> {
    /**
     * Tag name.
     * */
    private String name;
    /**
     * Similar artists associated with the tag.
     * */
    private ArrayList<Artist> similarArtists;
    /**
     * Similar tracks associated with the tag.
     * */
    private ArrayList<Track> similarTracks;
    /**
     * Similar tags. Each item is an Objects[2] which contains the tag
     * score (Double) in position '0' and tag ID (String) in position '1'.
     * */
    private ArrayList<Object[]> similarTags;
    /**
     * Default collection name.
     * */
    private String defaultCollection;
    /**
     * Default max number of results.
     * */
    private long defaultLimit;

    /**
     * Class constructor.
     * @param ellaConnection A connection to the Ella web service.
     * @param id The id of the tag.
     * @param collection The collection name of the tag.
     * */
    public Tag(final EllaConnection ellaConnection, final String id,
            final String collection) {
        this(ellaConnection, id, collection, Jella.DEFAULT_COLLECTION,
                Jella.DEFAULT_LIMIT, Jella.JELLA_CACHE_DIR,
                Jella.CACHE_ENABLE);
    }

    /**
     * Class constructor.
     * @param ellaConnection A connection to the Ella web service.
     * @param id The id of the tag.
     * @param collection The collection name of the tag.
     * @param defaultCollectionValue Default collection name to search.
     * @param defaultLimitValue max number of results.
     * @param jellaCacheDir The path to the cache directory.
     * @param cacheEnable A boolean that says if the cache is
     * enabled or not.
     * */
    public Tag(final EllaConnection ellaConnection, final String id,
            final String collection,
            final String defaultCollectionValue,
            final long defaultLimitValue,
            final String jellaCacheDir,
            final boolean cacheEnable) {
        super(ellaConnection, id, collection, jellaCacheDir, cacheEnable);
        this.method = "/tags/" + this.id + SearchObject.RESPONSE_TYPE;
        this.metadata = "_all";
        this.defaultCollection = defaultCollectionValue;
        this.defaultLimit = defaultLimitValue;
    }

    /**
     * Class constructor.
     * @param ellaConnection A connection to the Ella web service.
     * @param id The id of the tag.
     * */
    public Tag(final EllaConnection ellaConnection, final String id) {
        this(ellaConnection, id, "tags");
    }

    /**
     * @return The tag name.
     */
    public final String getName() {
        if (this.name == null) {
            this.name = this.getFieldValue("name");
        }
        return this.name;
    }

    /**
     * Sets the tag name.
     * @param nameValue A string with the tag name.
     * */
    public final void setName(final String nameValue) {
        this.name = nameValue;
    }

    /**
     * @return A list of similar artists associated with the tag
     * using limit = DEFAULTLIMIT,
     * collectionSIM = DEFAULTCOLLECTION,
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     */
    public final ArrayList<Artist> getSimilarArtists()
    throws ServiceException, IOException {
        return this.getSimilarArtists(
                this.defaultLimit,
                this.defaultCollection
        );
    }

    /**
     * @param limit Max number of results.
     * @param collectionSim Name of the collection it will look for similar.
     * @return A list of similar artists associated with the tag.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     */
    public final ArrayList<Artist> getSimilarArtists(final long limit,
            final String collectionSim)
            throws ServiceException, IOException {
        String artistMetadata = "artist,name,artist_popularity,"
            + "artist_location,recommendable,artist_decades1,artist_decades2,"
            + "artist_latlng,musicbrainz_artist_id,";

        String[] artistMetadataLinks = new String[]{
                "official_homepage_artist_url", "wikipedia_artist_url",
                "lastfm_artist_url", "myspace_artist_url",
                "spotify_artist_url", "itms_artist_url",
        "discogs_artist_url"};
        artistMetadata += Util.joinArray(artistMetadataLinks, ",");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("limit", Long.toString(limit));
        params.put("fetch_metadata", artistMetadata);
        String collectionRef = collectionSim;
        if (collectionRef == null) {
            collectionRef = this.collection;
        }

        String mtd = "/tags/" + this.id + "/similar/collections/"
        + collectionRef + "/artists" + SearchObject.RESPONSE_TYPE;

        JSONObject response = (JSONObject) this.request(mtd,
                params);
        JSONArray results = (JSONArray) response.get("results");
        this.similarArtists = new ArtistManager().getArtists(
                this.request.getEllaConnection(), results);
        return this.similarArtists;
    }

    /**
     * @return A list of similar tracks associated with the tag
     * using limit = DEFAULT_LIMIT, filter = null,
     * collectionSIM = DEFAULT_COLLECTION,
     * seeds = null, random = true.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     */
    public final ArrayList<Track> getSimilarTracks()
    throws ServiceException, IOException {
        return this.getSimilarTracks(
                this.defaultLimit,
                null,
                this.defaultCollection,
                null,
                true
        );
    }

    /**
     * @param limit Max number of results.
     * @param filter Search filters.
     * @param collectionSim Name of the collection it will look for similar.
     * @param seeds Similarities to fulfill.
     * @param random Set the similarity type as playlist or not.
     * @return A list of similar tracks associated with the tag.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     */
    public final ArrayList<Track> getSimilarTracks(final long limit,
            final String[] filter,
            final String collectionSim,
            final HashMap<String, HashMap<String, String>> seeds,
            final boolean random)
            throws ServiceException, IOException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("limit", Long.toString(limit));

        String trackMetadata = "track,artist_service_id,artist,"
            + "release_service_id,release,location,year,genre,"
            + "track_popularity,track_small_image,recommendable,"
            + "musicbrainz_track_id,spotify_track_uri,";
        String[] trackMetadataLinks = new String[]{
                "spotify_track_url",
                "grooveshark_track_url", "amazon_track_url",
                "itms_track_url", "hypem_track_url", "musicbrainz_track_url"};
        trackMetadata += Util.joinArray(trackMetadataLinks, ",");
        params.put("fetch_metadata", trackMetadata);

        if (filter != null) {
            params.put("filter", Util.joinArray(filter, " AND "));
        }

        params.put("seeds", this.collection + ":tag/" + this.id);

        this.setParamsSeedsField(seeds, params);

        if (random) {
            params.put("similarity_type", "playlist");
        }
        String mtd = "/tracks/similar_to" + SearchObject.RESPONSE_TYPE;
        String collectionRef = collectionSim;
        if (collectionRef == null) {
            collectionRef = this.collection;
        }
        JSONObject response = (JSONObject) this.request(mtd, collectionRef,
                params);
        JSONArray results = (JSONArray) response.get("results");
        this.similarTracks = new TrackManager().getTracks(
                this.request.getEllaConnection(),
                results
        );
        return similarTracks;
    }

    /**
     * @return A list of similar tags.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     */
    public final ArrayList<Object[]> getSimilarTags()
    throws ServiceException, IOException {
        if (this.similarTags == null) {
            this.similarTags = new ArrayList<Object[]>();
            String limit = "100";
            String mtd = "/tags/" + this.id
            + "/similar/collections/tags/tags" + SearchObject.RESPONSE_TYPE;
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("fetch_metadata", "tag_type");
            params.put("limit", limit);
            JSONObject response = (JSONObject) this.request(mtd, params);
            JSONArray results = (JSONArray) response.get("results");
            this.similarTags = new TagManager().getTags(
                    this.request.getEllaConnection(), results, "style");
        }
        return this.similarTags;
    }

    /**
     * Compares this one album to another.
     * @param object An Tag instance to be compared.
     * @return The value of the string comparation between IDs.
     * */
    public final int compareTo(final Tag object) {
        return this.id.compareTo(object.id);
    }

    /**
     * Checks if the IDs are the same.
     * @param object An Tag instance to be compared.
     * @return The value of the string equals comparation between IDs.
     * */
    public final boolean equals(final Object object) {
        if (this == object) {
            return true;
        } else if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        Tag tag = (Tag) object;
        return this.id.equals(tag.id);
    }

    /**
     * Overrides Object hasCode.
     * @return The hasCode of the ID.
     * */
    public final int hashCode() {
        return this.id.hashCode();
    }

    /**
     * @return The value of defaultLimit.
     * */
    public final long getDefaultLimit() {
        return this.defaultLimit;
    }

    /**
     * Modifies the defaultLimit value.
     * @param defaultLimitValue The value of defaultLimit.
     * */
    public final void setDefaultLimit(final long defaultLimitValue) {
        this.defaultLimit = defaultLimitValue;
    }

    /**
     * @return The name of defaultCollection.
     * */
    public final String getDefaultCollection() {
        return this.defaultCollection;
    }

    /**
     * Modifies the defaultCollection name.
     * @param defaultCollectionValue The name of defaultCollection.
     * */
    public final void setDefaultCollection(
            final String defaultCollectionValue) {
        this.defaultCollection = defaultCollectionValue;
    }
}

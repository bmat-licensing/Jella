
package com.bmat.ella;
import java.util.HashMap;
/**
 * Java Class Jella.
 * Ella Java API.
 * @author Harrington Joseph (Harph)
 * */
public class Jella {
    /**
     * An instance of Ella WS connection.
     * */
    private EllaConnection ellaConnection;
    /**
     * Constant.
     * Total number of results per page.
     * Default value 10.
     * */
    public static final int RESULTS_PER_PAGE = 10;
    /**
     * Total number of results per page.
     * */
    private int resultsPerPage;
    /**
     * Constant.
     * Default max number of results.
     * Default value 20.
     * */
    public static final long DEFAULT_LIMIT = 20;
    /**
     * Default max number of results.
     * */
    private long defaultLimit;
    /**
     * Constant.
     * Default collection name.
     * Defautl name "bmat".
     * */
    public static final String DEFAULT_COLLECTION = "bmat";
    /**
     * Default collection name.
     * */
    private String defaultCollection;
    /**
     * Constant.
     * Path to Jella cache directory.
     * Default path "../JELLA_CACHE_DIR".
     * */
    public static final String JELLA_CACHE_DIR = "../JELLA_CACHE_DIR";
    /**
     * Path to Jella cache directory.
     * */
    private String jellaCacheDir;
    /**
     * Constant.
     * Default max number of tag results.
     * */
    public static final int DEFAULT_TAG_LIMIT = 4;
    /**
     * Default max number of tag results.
     * */
    private int defaultTagLimit;
    /**
     * Constant.
     * Default tag type.
     * */
    public static final String DEFAULT_TAG_TYPE = "style";
    /**
     * Default tag type.
     * */
    private String defaultTagType;
    /**
     * Constant.
     * Default tag weight.
     * */
    public static final double DEFAULT_TAG_WEIGHT = 0.70;
    /**
     * Default tag weight.
     * */
    private double defaultTagWeight;
    /**
     * Constant.
     * Specifies if the cache enabled.
     * */
    public static final boolean CACHE_ENABLE = true;
    /**
     * Specifies if the cache enabled.
     * */
    private boolean cacheEnable;

    /**
     * Class constructor.
     * @param ellawsURL The URL to Ella WS.
     * @param username A String that represents the username.
     * @param password A String that represents the password.
     * */
    public Jella(final String ellawsURL, final String username,
            final String password) {
        this.ellaConnection = new EllaConnection(ellawsURL, username, password);
        this.resultsPerPage = Jella.RESULTS_PER_PAGE;
        this.defaultLimit = Jella.DEFAULT_LIMIT;
        this.defaultCollection = Jella.DEFAULT_COLLECTION;
        this.jellaCacheDir = Jella.JELLA_CACHE_DIR;
        this.defaultTagLimit = Jella.DEFAULT_TAG_LIMIT;
        this.defaultTagType = Jella.DEFAULT_TAG_TYPE;
        this.defaultTagWeight = Jella.DEFAULT_TAG_WEIGHT;
        this.cacheEnable = Jella.CACHE_ENABLE;
    }

    /**
     * @return The Ella connection instance.
     * */
    public final EllaConnection getEllaConnection() {
        return ellaConnection;
    }

    /**
     * Sets the EllaConnection instance.
     * @param ellaConnectionValue The Ella connection instance.
     * */
    public final void setEllaConnection(
            final EllaConnection ellaConnectionValue) {
        this.ellaConnection = ellaConnectionValue;
    }

    /**
     * @return The value of resultsPerPage.
     * */
    public final int getResultsPerPage() {
        return this.resultsPerPage;
    }

    /**
     * Modifies the resultsPerPage value.
     * @param resultsPerPageValue The value of resultsPerPage.
     * */
    public final void setResultsPerPage(final int resultsPerPageValue) {
        this.resultsPerPage = resultsPerPageValue;
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
     * @return The path to the cache directory.
     * */
    public final String getJellaCacheDir() {
        return this.jellaCacheDir;
    }

    /**
     * Modifies the path of the cache directory.
     * @param jellaCacheDirValue The path of the cache directory.
     * */
    public final void setJellaCacheDir(final String jellaCacheDirValue) {
        this.jellaCacheDir = jellaCacheDirValue;
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

    /**
     * @return The value of the defaultTagLimit.
     * */
    public final int getDefaultTagLimit() {
        return this.defaultTagLimit;
    }

    /**
     * Modifies the defaultTagLimit value.
     * @param defaultTagLimitValue The value of defaultTagLimit.
     * */
    public final void setDefaultTagLimit(final int defaultTagLimitValue) {
        this.defaultTagLimit = defaultTagLimitValue;
    }

    /**
     * @return The value of defaultTagType.
     * */
    public final String getDefaultTagType() {
        return this.defaultTagType;
    }

    /**
     * Modifies the defaultTagType value.
     * @param defaultTagTypeValue The value of defaultTagType.
     * */
    public final void setDefaultTagType(final String defaultTagTypeValue) {
        this.defaultTagType = defaultTagTypeValue;
    }

    /**
     * @return The value of defaultTagWeight.
     * */
    public final double getDefaultTagWeight() {
        return this.defaultTagWeight;
    }

    /**
     * Modifies the defaultTagWeight value.
     * @param defaultTagWeightValue The value of defaultTagWeight.
     * */
    public final void setDefaultTagWeight(final double defaultTagWeightValue) {
        this.defaultTagWeight = defaultTagWeightValue;
    }

    /**
     * @return The value of cacheEnable.
     * */
    public final boolean isCacheEnable() {
        return this.cacheEnable;
    }

    /**
     * Modifies the cacheEnable value.
     * @param cacheEnableValue The value of cacheEnable.
     * */
    public final void setCacheEnablet(final boolean cacheEnableValue) {
        this.cacheEnable = cacheEnableValue;
    }
    /**
     * Creates a TrackSeach instance with "search" as method type.
     * @param query A string that contains the value of the query.
     * @param collection The name of the queried collection.
     * @param fuzzy A Boolean value that indicates if is was a fuzzy search.
     * @param threshold The lowest Track score to consider.
     * @param filter A String array that contains the filters. i.e:
     * {"artist:u2", "release:leave behind"}.
     * @return An instance of TrackSearch.
     * */
    public final TrackSearch searchTracks(final String query,
            final String collection,
            final boolean fuzzy,
            final Double threshold,
            final String[] filter) {
        return new TrackSearch(this.ellaConnection,
                "search", query, collection, fuzzy, threshold, filter,
                this.resultsPerPage, this.getJellaCacheDir(),
                this.cacheEnable);
    }

    /**
     * Creates a TrackSeach instance with "resolve" as method type.
     * @param query A HasphMap with the keys "artist" and "track" that are
     * going to be queried.
     * @param collection The name of the queried collection.
     * @param fuzzy A Boolean value that indicates if it was a fuzzy search.
     * @param threshold The lowest Track score to consider.
     * @return An instance of TrackSearch.
     * */
    public final TrackSearch searchTracks(final HashMap<String, String> query,
            final String collection,
            final boolean fuzzy,
            final Double threshold) {
        return new TrackSearch(this.ellaConnection, query,
                collection, fuzzy, threshold,
                this.resultsPerPage, this.getJellaCacheDir(),
                this.cacheEnable);
    }

    /**
     * Creates a ArtistSearch instance with "search" as method type.
     * @param query A string that contains the value of the query.
     * @param collection The name of the queried collection.
     * @param fuzzy A Boolean value that indicates if it is a fuzzy search.
     * @param threshold The lowest Track score to consider.
     * @return An instance of ArtistSearch.
     * */
    public final ArtistSearch searchArtists(final String query,
            final String collection,
            final boolean fuzzy,
            final Double threshold) {
        return new ArtistSearch(this.ellaConnection, "search", query,
                collection, fuzzy, threshold,
                this.resultsPerPage, this.getJellaCacheDir(),
                this.cacheEnable);
    }

    /**
     * Creates a TagSearch instance with "search" as method type.
     * @param query A string that contains the value of the query.
     * @param collection The name of the queried collection.
     * @param fuzzy A Boolean value that indicates if is was a fuzzy search.
     * @return An instance of TagSearch.
     * */
    public final TagSearch searchTags(final String query,
            final String collection,
            final boolean fuzzy) {
        return new TagSearch(this.ellaConnection, "search", query,
                collection, fuzzy,
                this.resultsPerPage, this.getJellaCacheDir(),
                this.cacheEnable);
    }
}

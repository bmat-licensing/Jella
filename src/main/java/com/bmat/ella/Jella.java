
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
     * Total number of results per page.
     * Default value 10.
     * */
    private static int RESULTS_PER_PAGE = 10;
    /**
     * Default max number of results.
     * Default value 20.
     * */
    private static long DEFAULT_LIMIT = 20;
    /**
     * Default collection name.
     * Defautl name "bmat".
     * */
    private static String DEFAULT_COLLECTION = "bmat";
    /**
     * Path to Jella cache directory.
     * Default path "../JELLA_CACHE_DIR".
     * */
    private static String JELLA_CACHE_DIR = "../JELLA_CACHE_DIR";
    /**
     * Default max number of tag results.
     * */
    private static int DEFAULT_TAG_LIMIT = 4;
    /**
     * Default tag type.
     * */
    private static String DEFAULT_TAG_TYPE = "style";
    /**
     * Default tag weight.
     * */
    private static double DEFAULT_TAG_WEIGHT = 0.70;
    /**
     * Specifies if the cache enabled.
     * */
    private static boolean CACHE_ENABLE = true;

    /**
     * Class constructor.
     * @param ellawsURL The URL to Ella WS.
     * @param username A String that represents the username.
     * @param password A String that represents the password.
     * */
    public Jella(final String ellawsURL, final String username,
            final String password) {
        this.ellaConnection = new EllaConnection(ellawsURL, username, password);
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
     * @return The value of the constant RESULTS_PER_PAGE.
     * */
    public static int getResultsPerPage() {
        return RESULTS_PER_PAGE;
    }

    /**
     * Modifies the RESULTS_PER_PAGE value.
     * @param resultsPerPage The value of RESULTS_PER_PAGE.
     * */
    public static void setResultsPerPage(final int resultsPerPage) {
        RESULTS_PER_PAGE = resultsPerPage;
    }

    /**
     * @return The value of the constant DEFAULT_LIMIT.
     * */
    public static long getDefaultLimit() {
        return DEFAULT_LIMIT;
    }

    /**
     * Modifies the DEFAULT_LIMIT value.
     * @param defaultLimit The value of DEFAULT_LIMIT.
     * */
    public static void setDefaultLimit(final long defaultLimit) {
        DEFAULT_LIMIT = defaultLimit;
    }

    /**
     * @return The path to the cache directory.
     * */
    public static String getJellaCacheDir() {
        return JELLA_CACHE_DIR;
    }

    /**
     * Modifies the path of the cache directory.
     * @param jellaCacheDir The path of the cache directory.
     * */
    public static void setJellaCacheDir(final String jellaCacheDir) {
        JELLA_CACHE_DIR = jellaCacheDir;
    }

    /**
     * @return The name of the constant DEFAULT_COLLECTION.
     * */
    public static String getDefaultCollection() {
        return DEFAULT_COLLECTION;
    }

    /**
     * Modifies the DEFAULT_COLLECTION name.
     * @param defaultCollection The name of DEFAULT_COLLECTION.
     * */
    public static void setDefaultCollection(final String defaultCollection) {
        DEFAULT_COLLECTION = defaultCollection;
    }

    /**
     * @return The value of the constant DEFAULT_TAG_LIMIT.
     * */
    public static int getDefaultTagLimit() {
        return DEFAULT_TAG_LIMIT;
    }

    /**
     * Modifies the DEFAULT_TAG_LIMIT value.
     * @param defaultTagLimit The value of DEFAULT_TAG_LIMIT.
     * */
    public static void setDefaultTagLimit(final int defaultTagLimit) {
        DEFAULT_TAG_LIMIT = defaultTagLimit;
    }

    /**
     * @return The value of the constant DEFAULT_TAG_TYPE.
     * */
    public static String getDefaultTagType() {
        return DEFAULT_TAG_TYPE;
    }

    /**
     * Modifies the DEFAULT_TAG_TYPE value.
     * @param defaultTagType The value of DEFAULT_TAG_TYPE.
     * */
    public static void setDefaultTagType(final String defaultTagType) {
        DEFAULT_TAG_TYPE = defaultTagType;
    }

    /**
     * @return The value of the constant DEFAULT_TAG_WEIGHT.
     * */
    public static double getDefaultTagWeight() {
        return DEFAULT_TAG_WEIGHT;
    }

    /**
     * Modifies the DEFAULT_TAG_WEIGHT value.
     * @param defaultTagWeight The value of DEFAULT_TAG_WEIGHT.
     * */
    public static void setDefaultTagWeight(final double defaultTagWeight) {
        DEFAULT_TAG_WEIGHT = defaultTagWeight;
    }

    /**
     * @return The value of the constant CACHE_ENABLE.
     * */
    public static boolean isCacheEnable() {
        return CACHE_ENABLE;
    }

    /**
     * Modifies the CACHE_ENABLE value.
     * @param cacheEnable The value of CACHE_ENABLE.
     * */
    public static void setCacheEnablet(final boolean cacheEnable) {
        CACHE_ENABLE = cacheEnable;
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
                "search", query, collection, fuzzy, threshold, filter);
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
                collection, fuzzy, threshold);
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
                collection, fuzzy, threshold);
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
                collection, fuzzy);
    }
}

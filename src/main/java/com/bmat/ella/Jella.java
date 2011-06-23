
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
    public static int getRESULTS_PER_PAGE() {
        return RESULTS_PER_PAGE;
    }

    /**
     * Modifies the RESULTS_PER_PAGE value.
     * @param rESULTS_PER_PAGE The value of RESULTS_PER_PAGE.
     * */
    public static void setRESULTS_PER_PAGE(int rESULTS_PER_PAGE) {
        RESULTS_PER_PAGE = rESULTS_PER_PAGE;
    }

    /**
     * @return The value of the constant DEFAULT_LIMIT.
     * */
    public static long getDEFAULT_LIMIT() {
        return DEFAULT_LIMIT;
    }

    /**
     * Modifies the DEFAULT_LIMIT value.
     * @param dEFAULT_LIMIT The value of DEFAULT_LIMIT.
     * */
    public static void setDEFAULT_LIMIT(long dEFAULT_LIMIT) {
        DEFAULT_LIMIT = dEFAULT_LIMIT;
    }

    /**
     * @return The path to the cache directory.
     * */
    public static String getJELLA_CACHE_DIR() {
        return JELLA_CACHE_DIR;
    }

    /**
     * Modifies the path of the cache directory.
     * @param jELLA_CACHE_DIR The path of the cache directory.
     * */
    public static void setJELLA_CACHE_DIR(String jELLA_CACHE_DIR) {
        JELLA_CACHE_DIR = jELLA_CACHE_DIR;
    }

    /**
     * @return The name of the constant DEFAULT_COLLECTION.
     * */
    public static String getDEFAULT_COLLECTION() {
        return DEFAULT_COLLECTION;
    }

    /**
     * Modifies the DEFAULT_COLLECTION name.
     * @param dEFAULT_COLLECTION The name of DEFAULT_COLLECTION.
     * */
    public static void setDEFAULT_COLLECTION(String dEFAULT_COLLECTION) {
        DEFAULT_COLLECTION = dEFAULT_COLLECTION;
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

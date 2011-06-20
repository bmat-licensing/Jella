
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

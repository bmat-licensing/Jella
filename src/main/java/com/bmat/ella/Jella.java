
package com.bmat.ella;

import java.io.IOException;
import java.util.HashMap;

public class Jella {

    private EllaConnection ellaConnection;

    public Jella(final String ellaws, final String username, final String password) {
        this.ellaConnection = new EllaConnection(ellaws, username, password);
    }

    public final EllaConnection getEllaConnection() {
        return ellaConnection;
    }

    public void setEllaConnection(EllaConnection ellaConnection) {
        this.ellaConnection = ellaConnection;
    }

    public final TrackSearch searchTracks(final String query,
            final String collection,
            final boolean fuzzy,
            final Double threshold,
            final String[] filter)
    throws ServiceException, IOException {
        return new TrackSearch(this.ellaConnection,
                "search",query, collection, fuzzy, threshold, filter);
    }

    /* method = resolve */
    public final TrackSearch searchTracks(final HashMap<String, String> query,
            final String collection,
            final boolean fuzzy,
            final Double threshold) throws ServiceException, IOException {
        return new TrackSearch(this.ellaConnection, query,
                collection, fuzzy, threshold);
    }

    public final ArtistSearch searchArtists(final String query,
            final String collection,
            final boolean fuzzy,
            final Double threshold) throws Exception{
        return new ArtistSearch(this.ellaConnection, "search", query,
                collection, fuzzy, threshold);
    }

    public final TagSearch searchTags(final String query,
            final String collection,
            final boolean fuzzy){
        return new TagSearch(this.ellaConnection, "search", query,
                collection, fuzzy);
    }
}

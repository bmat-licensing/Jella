
package com.bmat.ella;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONArray;
/**
 * Java Class TrackSearch.
 * Search for Tracks.
 * @author Harrington Joseph (Harph)
 * */
public class TrackSearch extends Search {
    /**
     * Class constructor.
     * @param ellaConnection A connection to the Ella web service.
     * @param method A String that contains the name of the type of search
     * (search, match).
     * @param query A string that contains the value of the query.
     * @param collection The name of the queried collection.
     * @param fuzzy A Boolean value that indicates if is was a fuzzy search.
     * @param threshold The lowest Track score to consider. In case of null,
     * it will be considered as 0.
     * @param filter A String array that contains the filters. i.e:
     * {"artist:u2", "release:leave behind"}.
     * */
    public TrackSearch(final EllaConnection ellaConnection,
            final String method,
            final String query,
            final String collection,
            final boolean fuzzy,
            final Double threshold,
            final String[] filter) {
        super(ellaConnection, collection);
        String qry = query;
        this.fuzzy = fuzzy;
        if (threshold != null) {
            this.threshold = threshold;
        } else {
            this.threshold = 0.0;
        }
        this.searchTerms = new HashMap<String, String>();
        String mtd;
        if (!this.fuzzy) { // it's a search
            if (filter == null) {
                this.searchTerms.put("q", "trackartist:" + qry);
            } else {
                if (qry == null) {
                    qry = "";
                } else if (!qry.equals("") && filter.length > 0) {
                    qry += " AND ";
                }
                qry += Util.joinArray(filter, " AND ");
                this.searchTerms.put("q", qry);
            }
            searchTerms.put("limit", "10");
            if (method.equals("search") || method.equals("match")) {
                mtd = method;
            } else {
                mtd = "search";
            }
        } else {
            this.searchTerms.put("q", qry);
            this.searchTerms.put("limit", "30");
            this.searchTerms.put("fuzzy", "true");
            mtd = "match";
        }
        this.initialize("/tracks/" + mtd);
        this.searchTerms.put("fetch_metadata", this.metadata);
    }

    /**
     * Class constructor.
     * It creates an instance for resolve search.
     * @param ellaConnection A connection to the Ella web service.
     * @param query A HasphMap with the keys "artist" and "track" that are
     * going to be queried.
     * @param collection The name of the queried collection.
     * @param fuzzy A Boolean value that indicates if it was a fuzzy search.
     * @param threshold The lowest Track score to consider. In case of null,
     * it will be considered as 0.
     * */
    public TrackSearch(final EllaConnection ellaConnection,
            final HashMap<String, String> query,
            final String collection, final boolean fuzzy,
            final Double threshold) {
        super(ellaConnection, collection);
        this.initialize("/tracks/resolve");
        this.fuzzy = fuzzy;
        if (threshold != null) {
            this.threshold = threshold;
        } else {
            this.threshold = 0.0;
        }
        String artist = (String) query.get("artist");
        String track = (String) query.get("track");

        this.searchTerms = new HashMap<String, String>();
        if (artist != null) {
            this.searchTerms.put("artist", artist);
        }
        if (track != null) {
            this.searchTerms.put("track", track);
        }
        this.searchTerms.put("limit", "100");
        this.searchTerms.put("fetch_metadata", this.metadata);
    }

    /**
     * Sets the values of method, metadata and metadataLinks.
     * @param method A String with the type of search.
     * */
    private void initialize(final String method) {
        this.method = method + SearchObject.RESPONSE_TYPE;
        this.metadataLinks = new String[]{"spotify_track_url",
                "grooveshark_track_url", "amazon_track_url",
                "musicbrainz_track_url", "hypem_track_url"};
        this.metadata = "track,artist_service_id,artist,"
            + "release_service_id,release,location,year,genre,"
            + "track_popularity,track_small_image,recommendable,"
            + "musicbrainz_track_id,spotify_track_uri,"
            + Util.joinArray(this.metadataLinks, ",");
    }

    /**
     * @param pageIndex page number.
     * @return An ArrayList of tracks that match the search that are on the
     * pageIndex.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    public final ArrayList<Track> getPage(final long pageIndex)
    throws ServiceException, IOException {
        long pageNumber;
        if (pageIndex > 0) {
            pageNumber = pageIndex - 1;
        } else {
            pageNumber = 0;
        }
        return this.getResults(this.retrievePage(pageNumber));
    }

    /**
     * @return An ArrayList of tracks that match the search that are on the
     * next page.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    public final ArrayList<Track> getNextPage()
    throws ServiceException, IOException {
        return this.getResults(this.retrieveNextPage());
    }

    /**
     * @param jsonResults the result node of the response.
     * @return An ArrayList of the tracks contained in the jsonResults.
     * */
    private ArrayList<Track> getResults(final JSONArray jsonResults) {
        if (jsonResults == null) {
            return null;
        }
        ArrayList<Track> results = new TrackManager().getTracks(
                this.request.getEllaConnection(), jsonResults, this.threshold);
        return results;
    }
}

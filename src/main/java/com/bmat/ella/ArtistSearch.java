
package com.bmat.ella;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONArray;
/**
 * Java Class ArtistSearch.
 * Search for Artists.
 * @author Harrington Joseph (Harph)
 * */
public class ArtistSearch extends Search {

    /**
     * Class constructor.
     * @param ellaConnection A connection to the Ella web service.
     * @param method A String that contains the name of the type of search
     * (search, resolve, match).
     * @param query A string that contains the value of the query.
     * @param collection The name of the queried collection.
     * @param fuzzy A Boolean value that indicates if it is a fuzzy search.
     * @param threshold The lowest Track score to consider. In case of null,
     * it will be considered as 0.
     * */
    public ArtistSearch(final EllaConnection ellaConnection,
            final String method, final String query,
            final String collection, final boolean fuzzy,
            final Double threshold) {
        super(ellaConnection, collection);
        this.fuzzy = fuzzy;
        if (threshold != null) {
            this.threshold = threshold;
        } else {
            this.threshold = 0.0;
        }
        this.searchTerms = new HashMap<String, String>();
        this.metadataLinks = new String[]{"official_homepage_artist_url",
                "wikipedia_artist_url", "lastfm_artist_url",
                "myspace_artist_url", "spotify_artist_url",
        "discogs_artist_url"};
        this.metadata = "name,relevance,recommendable,artist_decades1,"
            + "artist_decades2,artist_location,artist_latlng,"
            + "artist_popularity,musicbrainz_artist_id,"
            + Util.joinArray(this.metadataLinks, ",");
        String mtd = "/artists/";
        if (method.equals("resolve")) {
            mtd += "resolve";
            searchTerms.put("artist", query);
            searchTerms.put("limit", "100");
            searchTerms.put("fetch_metadata", this.metadata);
        } else if (!this.fuzzy) { // it's a search
            if (method.equals("search") || method.equals("match")) {
                mtd += method;
            } else {
                mtd += "search";
            }
            searchTerms.put("q", query);
            searchTerms.put("limit", "100");
            searchTerms.put("fetch_metadata", this.metadata);
        } else {
            mtd += "match";
            searchTerms.put("q", query);
            searchTerms.put("limit", "30");
            searchTerms.put("fuzzy", "true");
        }
        searchTerms.put("fetch_metadata", this.metadata);
        this.method = mtd + SearchObject.RESPONSE_TYPE;
    }

    /**
     * @param pageIndex page number.
     * @return An ArrayList of artists that match the search that are on the
     * pageIndex.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    public final ArrayList<Artist> getPage(final long pageIndex)
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
     * @return An ArrayList of artists that match the search that are on the
     * next page.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    public final ArrayList<Artist> getNextPage()
    throws ServiceException, IOException {
        return this.getResults(this.retrieveNextPage());
    }

    /**
     * @param jsonResults the result node of the response.
     * @return An ArrayList of the artists contained in the jsonResults.
     * */
    private ArrayList<Artist> getResults(final JSONArray jsonResults) {
        if (jsonResults == null) {
            return null;
        }
        ArrayList<Artist> results = new ArtistManager().getArtists(
                this.request.getEllaConnection(), jsonResults,
                this.method, this.threshold, this.fuzzy,
                this.metadataLinks);
        return results;
    }
}

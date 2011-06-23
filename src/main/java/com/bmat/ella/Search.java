
package com.bmat.ella;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
/**
 * Java Class Search.
 * Represents a web service request.
 * @author Harrington Joseph (Harph)
 * */
public abstract class Search extends SearchObject {
    /**
     * Value that indicates if it was a fuzzy search.
     * */
    protected boolean fuzzy = false;
    /**
     * The search params.
     * */
    protected HashMap<String, String> searchTerms;
    /**
     * The lowest Track score to consider.
     * */
    protected Double threshold;
    /**
     * Number of last page retrieved.
     * */
    private long lastPageIndex;
    /**
     * The total number of results.
     * */
    private Long hits;

    /**
     * Class constructor.
     * @param ellaConnection A connection to the Ella web service.
     * @param collection The collection name.
     * */
    public Search(final EllaConnection ellaConnection,
            final String collection) {
        super(ellaConnection, collection);
        this.lastPageIndex = -1;
        this.hits = null;
    }

    /**
     * @param pageIndex page number.
     * @return An ArrayList that match the search on page number "pageIndex".
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    @SuppressWarnings("rawtypes")
    public abstract ArrayList getPage(long pageIndex)
    throws ServiceException, IOException;

    /**
     * @return An ArrayList that match the search on next page.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    @SuppressWarnings("rawtypes")
    public abstract ArrayList getNextPage()
    throws ServiceException, IOException;

    /**
     * @param pageIndex page number.
     * @return An JSONArray with the response results for the page
     * number pageIndex.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    protected final JSONArray retrievePage(final long pageIndex)
    throws ServiceException, IOException {
        HashMap<String, String> params = this.getParams();
        if (this.method.indexOf("resolve") == -1) {
            long offset = 0;
            if (pageIndex != 0) {
                offset = Jella.getRESULTS_PER_PAGE() * pageIndex;
            }
            params.put("offset", Long.toString(offset));
        }
        JSONObject response = (JSONObject) this.request(params);
        if (response != null) {
            return (JSONArray) response.get("results");
        } else {
            return null;
        }
    }

    /**
     * @return An JSONArray with the response results for next page.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    protected final JSONArray retrieveNextPage()
    throws ServiceException, IOException {
        this.lastPageIndex += 1;
        return this.retrievePage(this.lastPageIndex);
    }

    /**
     * @return The total number of results.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    protected final long getTotalResultCount()
    throws ServiceException, IOException {
        if (this.hits != null) {
            return this.hits;
        }
        HashMap<String, String> params = this.getParams();
        if (this.method.indexOf("resolve") == -1) {
            params.put("offset", "0");
            params.put("fetch_metadata", "_none");
        } else {
            params.remove("fetch_metadata");
        }
        params.put("limit", "1");
        JSONObject response = (JSONObject) this.request(params);
        JSONObject stats = (JSONObject) response.get("stats");
        long totalHits;
        if (this.collection != null) {
            totalHits = new Long(stats.get("total_hits").toString());
        } else {
            totalHits = 0;
            JSONObject jsonHits = (JSONObject) stats.get("total_hits");
            for (Object key : jsonHits.keySet()) {
                totalHits += new Long(jsonHits.get(key.toString()).toString());
            }
        }
        this.hits = totalHits;
        return this.hits;
    }

    /**
     * @return a HaspMap generated as a copy of searchTerms.
     * */
    private HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<String, String>();
        for (String key : this.searchTerms.keySet()) {
            params.put(key, this.searchTerms.get(key).toString());
        }
        return params;
    }
}

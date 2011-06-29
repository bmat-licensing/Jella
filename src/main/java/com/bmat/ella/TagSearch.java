
package com.bmat.ella;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
/**
 * Java Class TagSearch.
 * Search for Tags.
 * @author Harrington Joseph (Harph)
 * */
public class TagSearch extends Search {

    /**
     * Class constructor.
     * @param ellaConnection A connection to the Ella web service.
     * @param method A String that contains the name of the type of search
     * (search, match).
     * @param query A string that contains the value of the query.
     * @param collection The name of the queried collection.
     * @param fuzzy A Boolean value that indicates if is was a fuzzy search.
     * */
    public TagSearch(final EllaConnection ellaConnection,
            final String method, final String query,
            final String collection, final boolean fuzzy) {
        this(ellaConnection, method, query, collection, fuzzy,
                Jella.RESULTS_PER_PAGE, Jella.JELLA_CACHE_DIR,
                Jella.CACHE_ENABLE);
    }

    /**
     * Class constructor.
     * @param ellaConnection A connection to the Ella web service.
     * @param method A String that contains the name of the type of search
     * (search, match).
     * @param query A string that contains the value of the query.
     * @param collection The name of the queried collection.
     * @param fuzzy A Boolean value that indicates if is was a fuzzy search.
     * @param resultsPerPage The value of resultsPerPage.
     * @param jellaCacheDir The path to the cache directory.
     * @param cacheEnable A boolean that says if the cache is
     * enabled or not.
     * */
    public TagSearch(final EllaConnection ellaConnection,
            final String method, final String query,
            final String collection, final boolean fuzzy,
            final int resultsPerPage,
            final String jellaCacheDir, final boolean cacheEnable) {
        super(ellaConnection, collection, resultsPerPage,
                jellaCacheDir, cacheEnable);
        this.fuzzy = fuzzy;
        this.searchTerms = new HashMap<String, String>();
        this.metadata = "_all";
        searchTerms.put("q", query);
        searchTerms.put("fetch_metadata", this.metadata);
        String mtd = "/tags/";
        if (!this.fuzzy) {
            if (method == null) {
                mtd += "search";
            } else {
                mtd += method;
            }
            searchTerms.put("limit", "10");
        } else {
            mtd += "match";
            searchTerms.put("limit", "30");
            searchTerms.put("fuzzy", "True");
        }
        this.method = mtd + SearchObject.RESPONSE_TYPE;
    }

    /**
     * @param pageIndex page number.
     * @return An ArrayList of tags that match the search that are on the
     * pageIndex.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    public final ArrayList<Tag> getPage(final long pageIndex)
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
     * @return An ArrayList of tags that match the search that are on the
     * next page.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * @throws ServiceException When Ella WS response fails.
     * */
    public final ArrayList<Tag> getNextPage()
    throws ServiceException, IOException {
        return this.getResults(this.retrieveNextPage());
    }

    /**
     * @param jsonResults the result node of the response.
     * @return An ArrayList of the tags contained in the jsonResults.
     * */
    private ArrayList<Tag> getResults(final JSONArray jsonResults) {
        if (jsonResults == null) {
            return null;
        }

        ArrayList<Tag> results = new ArrayList<Tag>();
        for (Object json : jsonResults) {
            JSONObject jsonTag = (JSONObject) json;
            JSONObject jsonEntity = (JSONObject) jsonTag.get("entity");
            JSONObject jsonMetadata = (JSONObject) jsonEntity.get("metadata");
            String tagId = (String) jsonEntity.get("id");
            if (tagId == null || tagId.equals("")) {
                continue;
            }
            String collection = (String) jsonEntity.get("collection");
            Object relevance = jsonTag.get("score");
            double score = 0.0;
            if (relevance != null && !relevance.toString().equals("")) {
                score = new Double(relevance.toString());
            }
            if (this.fuzzy && score < 0.4) {
                continue;
            }
            if (this.method.indexOf("match") != -1 && score < 0.7) {
                continue;
            }
            Tag tag = new Tag(this.request.getEllaConnection(), tagId,
                    collection);
            tag.setName((String) jsonMetadata.get("name"));
            results.add(tag);
        }
        return results;
    }
}

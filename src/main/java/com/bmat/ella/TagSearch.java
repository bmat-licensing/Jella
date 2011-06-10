package com.bmat.ella;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class TagSearch extends Search {
    public TagSearch(final EllaConnection ellaConnection, final String method, final String query, final String collection, final boolean fuzzy) {
        super(ellaConnection, collection);
        this.fuzzy = fuzzy;
        this.searchTerms = new HashMap<String, String>();
        this.metadata = "_all";
        searchTerms.put("q", query);
        searchTerms.put("fetch_metadata", this.metadata);
        String mtd = "/tags/";
        if(!this.fuzzy) {
            mtd += method == null ? "search" : method;
            searchTerms.put("limit", "10");
        }else {
            mtd += "match";
            searchTerms.put("limit", "30");
            searchTerms.put("fuzzy", "True");
        }
        this.method = mtd + this.RESPONSE_TYPE;
    }

    @Override
    public ArrayList<Tag> getPage(long pageIndex) throws ServiceException, IOException {
        pageIndex = pageIndex > 0 ? pageIndex - 1 : 0;
        return this.getResult(this.retrieveNextPage());
    }

    @Override
    public ArrayList<Tag> getNextPage() throws ServiceException, IOException {
        return this.getResult(this.retrieveNextPage());
    }

    private ArrayList<Tag> getResult(JSONArray jsonResults) {
        if(jsonResults == null) {
            return null;
        }

        ArrayList<Tag> results = new ArrayList<Tag>();
        for(Object json : jsonResults) {
            JSONObject jsonTag = (JSONObject) json;
            JSONObject jsonEntity = (JSONObject) jsonTag.get("entity");
            JSONObject jsonMetadata = (JSONObject) jsonEntity.get("metadata");
            String tagId = (String) jsonEntity.get("id");
            if(tagId == null || tagId.equals("")) {
                continue;
            }
            String collection = (String) jsonEntity.get("collection");
            Object relevance = jsonTag.get("score");
            double score = relevance != null && !relevance.toString().equals("")? new Double(relevance.toString()) : 0.0;
            if(this.fuzzy && score < 0.4) {
                continue;
            }
            if(this.method.indexOf("match") != -1 && score < 0.7) {
                continue;
            }
            Tag tag = new Tag(this.request.getEllaConnection(), tagId, collection);
            tag.setName((String) jsonMetadata.get("name"));
            results.add(tag);
        }
        return results;
    }
}

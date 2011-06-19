
package com.bmat.ella;

import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
/**
 * Java Class BaseObject.
 * Represents a BMAT object.
 * @author Harrington Joseph (Harph)
 * */
public abstract class BaseObject extends SearchObject {
    /**
     * Object ID.
     * */
    protected String id;
    /**
     * Object recommend.
     * */
    protected Boolean recommend;
    /**
     * Object mbid.
     * */
    protected String mbid;
    /**
     * Object related links.
     * */
    protected HashMap<String, String[]> links;
    /**
     * Object JSON source.
     * */
    protected JSONObject json;
    /**
     * Default max number of results.
     * */
    protected final long DEFAULT_LIMIT = 20;
    /**
     * Default collection name.
     * */
    protected final String DEFAULT_COLLECTION = "bmat";

    /**
     * Class constructor.
     * @param ellaConnection A connection to the Ella web service.
     * @param id The id of the song.
     * @param collection The collection name of the album.
     * */
    public BaseObject(final EllaConnection ellaConnection, final String id, final String collection) {
        super(ellaConnection, collection);
        this.id = id;
    }

    public final String getId() {
        return id;
    }

    public final void setId(final String idValue) {
        this.id = idValue;
    }

    public final Boolean isRecommend() {
        if (this.recommend == null) {
            this.setRecommend(this.getFieldValue("recommendable"));
        }
        return recommend;
    }

    public final void setRecommend(final Object recommendValue) {
        if (recommendValue != null) {
            this.recommend = new Boolean(recommendValue.toString().toLowerCase());
        } else {
            this.recommend = true;
        }
    }

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        if (mbid != null) {
            this.mbid = mbid;
        } else {
            this.mbid = "";
        }
    }

    public final HashMap<String, String[]> getLinks() {
        if (this.links == null) {
            this.links = new HashMap<String, String[]>();
            try {
                this.obtainJson();
                JSONObject jsonMetadata = (JSONObject) this.json.get("metadata");
                for (String link : this.metadataLinks) {
                    Object linkObject = jsonMetadata.get(link);
                    if (linkObject instanceof String) {
                        this.setLinks(link, (String)jsonMetadata.get(link).toString());
                    } else if (linkObject !=null) {
                        this.setLinks(link, (JSONArray) jsonMetadata.get(link));
                    }
                }
            } catch(Exception e) {
                return this.links;
            }
        }
        return links;
    }

    public final void setLinks(final String service, final JSONArray linksValue) {
        if (this.links == null) {
            this.links = new HashMap<String, String[]>();
        }
        String[] serviceLinks = new String[linksValue.size()]; 
        for (int i = 0; i < serviceLinks.length; i++) {
            serviceLinks[i] = linksValue.get(i).toString();
        }
        this.links.put(service, serviceLinks);
    }

    public final void setLinks(final String service, final String link) {
        if (this.links == null) {
            this.links = new HashMap<String, String[]>();
        }
        this.links.put(service, new String[]{link});
    }

    public final JSONObject getJson() {
        return json;
    }

    public final void setJson(final JSONObject jsonValue) {
        this.json = jsonValue;
    }

    private final void obtainJson() {
        if (this.json == null) {
            try {
                HashMap<String, String> fetchMetadata =
                    new HashMap<String, String>();
                fetchMetadata.put("fetch_metadata", this.metadata);
                JSONArray response = (JSONArray) this.request(fetchMetadata);
                this.json = (JSONObject) response.get(0);
            } catch(Exception e) {
                this.json = null;
            }
        }
    }

    protected final String getFieldValue(final String nameValue) {
        String fieldValue = null;
        this.obtainJson();
        JSONObject jsonMetadata = (JSONObject) this.json.get("metadata");
        Object objValue = jsonMetadata.get(nameValue);
        fieldValue = objValue != null ? objValue.toString() : null;
        return fieldValue;
    }

    protected final JSONArray getFieldValues(final String nameValue) {
        this.obtainJson();
        JSONObject jsonMetadata = (JSONObject) this.json.get("metadata");
        JSONArray objValue = (JSONArray) jsonMetadata.get(nameValue);
        return objValue;
    }
}

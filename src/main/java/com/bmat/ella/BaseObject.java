
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
     * @param idValue The id of the song.
     * @param collection The collection name of the album.
     * */
    public BaseObject(final EllaConnection ellaConnection,
            final String idValue, final String collection) {
        super(ellaConnection, collection);
        this.id = idValue;
    }

    /**
     * @return The object id.
     * */
    public final String getId() {
        return id;
    }

    /**
     * Sets the object id.
     * @param idValue The object id value.
     * */
    public final void setId(final String idValue) {
        this.id = idValue;
    }

    /**
     * @return If the object is recommended.
     * */
    public final Boolean isRecommend() {
        if (this.recommend == null) {
            this.setRecommend(this.getFieldValue("recommendable"));
        }
        return recommend;
    }

    /**
     * Sets the object recommend.
     * @param recommendValue The recommend value.
     * */
    public final void setRecommend(final Object recommendValue) {
        if (recommendValue != null) {
            this.recommend = new Boolean(
                    recommendValue.toString().toLowerCase());
        } else {
            this.recommend = true;
        }
    }

    /**
     * @return The object mbid.
     * */
    protected String getMbid() {
        return mbid;
    }

    /**
     * Sets the object mbid.
     * @param mbidValue The link key or name.
     * */
    public final void setMbid(final String mbidValue) {
        if (mbidValue != null) {
            this.mbid = mbidValue;
        } else {
            this.mbid = "";
        }
    }

    /**
     * @return The object links.
     * */
    public final HashMap<String, String[]> getLinks() {
        if (this.links == null) {
            this.links = new HashMap<String, String[]>();
            try {
                this.obtainJson();
                JSONObject jsonMetadata =
                    (JSONObject) this.json.get("metadata");
                for (String link : this.metadataLinks) {
                    Object linkObject = jsonMetadata.get(link);
                    if (linkObject instanceof String) {
                        this.setLinks(link,
                                (String) jsonMetadata.get(link).toString());
                    } else if (linkObject != null) {
                        this.setLinks(link,
                                (JSONArray) jsonMetadata.get(link));
                    }
                }
            } catch (Exception e) {
                return this.links;
            }
        }
        return links;
    }

    /**
     * Sets the object links.
     * @param service The link key or name.
     * @param linksValue The values of the Object links.
     * */
    public final void setLinks(final String service,
            final JSONArray linksValue) {
        if (this.links == null) {
            this.links = new HashMap<String, String[]>();
        }
        String[] serviceLinks = new String[linksValue.size()];
        for (int i = 0; i < serviceLinks.length; i++) {
            serviceLinks[i] = linksValue.get(i).toString();
        }
        this.links.put(service, serviceLinks);
    }

    /**
     * Sets the an object link.
     * @param service The link key or name.
     * @param link A value of the Object links.
     * */
    public final void setLinks(final String service, final String link) {
        if (this.links == null) {
            this.links = new HashMap<String, String[]>();
        }
        this.links.put(service, new String[]{link});
    }

    /**
     * @return The object JSON representation.
     * */
    public final JSONObject getJson() {
        return json;
    }

    /**
     * Sets the JSON representation.
     * @param jsonValue A JSONObject that represents the object.
     * */
    public final void setJson(final JSONObject jsonValue) {
        this.json = jsonValue;
    }

    /**
     * Executes a request to get the JSON representation.
     * */
    private void obtainJson() {
        if (this.json == null) {
            try {
                HashMap<String, String> fetchMetadata =
                    new HashMap<String, String>();
                fetchMetadata.put("fetch_metadata", this.metadata);
                JSONArray response = (JSONArray) this.request(fetchMetadata);
                this.json = (JSONObject) response.get(0);
            } catch (Exception e) {
                this.json = null;
            }
        }
    }

    /**
     * @param nameValue The field name.
     * @return The value for the nameValue Field.
     * */
    protected final String getFieldValue(final String nameValue) {
        String fieldValue = null;
        this.obtainJson();
        JSONObject jsonMetadata = (JSONObject) this.json.get("metadata");
        Object objValue = jsonMetadata.get(nameValue);
        fieldValue = null;
        if (objValue != null) {
            fieldValue = objValue.toString();
        }
        return fieldValue;
    }

    /**
     * @param nameValue The field name.
     * @return The values for the nameValue Field.
     * */
    protected final JSONArray getFieldValues(final String nameValue) {
        this.obtainJson();
        JSONObject jsonMetadata = (JSONObject) this.json.get("metadata");
        JSONArray objValue = (JSONArray) jsonMetadata.get(nameValue);
        return objValue;
    }
}

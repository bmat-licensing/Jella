
package com.bmat.ella;

import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
/**
 * Java Class TrackManager.
 * Manager of Tag results.
 * @author Harrington Joseph (Harph)
 * */
public class TagManager {
    /**
     * Extract all the data from the JSONArray and create an ArrayList of tags.
     * @param ellaConnection A connection to the Ella web service.
     * @param results The JSONArray that contains the WS response.
     * @param defaultTagType A string that contain the tag type allowed.
     * @return An ArrayList of Object[2] which contains the tag
     * score (Double) in position '0' and tag ID (String) in position '1'.
     * */
    public final ArrayList<Object[]> getTags(
            final EllaConnection ellaConnection,
            final JSONArray results,
            final String defaultTagType) {
        return this.getTags(ellaConnection, results, defaultTagType, null);
    }

    /**
     * Extract all the data from the JSONArray and create an ArrayList of tags.
     * @param ellaConnection A connection to the Ella web service.
     * @param results The JSONArray that contains the WS response.
     * @param tagWeight The lowest tag weight allowed.
     * @return An ArrayList of Object[2] which contains the tag
     * score (Double) in position '0' and tag ID (String) in position '1'.
     * */
    public final ArrayList<Object[]> getTags(
            final EllaConnection ellaConnection,
            final JSONArray results,
            final double tagWeight) {
        return this.getTags(ellaConnection, results, null, tagWeight);
    }

    /**
     * Extract all the data from the JSONArray and create an ArrayList of tags.
     * @param ellaConnection A connection to the Ella web service.
     * @param results The JSONArray that contains the WS response.
     * @param defaultTagType A string that contain the tag type allowed.
     * @param tagWeight The lowest tag weight allowed.
     * @return An ArrayList of Object[2] which contains the tag
     * score (Double) in position '0' and tag ID (String) in position '1'.
     * */
    public final ArrayList<Object[]> getTags(
            final EllaConnection ellaConnection,
            final JSONArray results,
            final String defaultTagType,
            final Double tagWeight) {
        ArrayList<Object[]> tags = new ArrayList<Object[]>();
        for (Object json : results) {
            JSONObject jsonTag = (JSONObject) json;
            JSONObject jsonEntity = (JSONObject) jsonTag.get("entity");
            JSONObject jsonMetadata = (JSONObject) jsonEntity.get(
            "metadata");
            String tagId = (String) jsonEntity.get("id");
            if (tagId == null || tagId.equals("")) {
                continue;
            }
            String tagType = (String) jsonMetadata.get("tag_type");
            if (defaultTagType != null && tagType != null
                    && !tagType.equals(defaultTagType)) {
                continue;
            }
            Object relevance = jsonTag.get("score");
            if (tagWeight != null && relevance == null) {
                continue;
            }
            double score = 0.0;
            if (relevance != null && !relevance.toString().equals("")) {
                score = new Double(relevance.toString());
            }
            if (tagWeight != null && score < tagWeight) {
                break;
            }
            tags.add(new Object[]{score, tagId});
        }
        return tags;
    }
}


package com.bmat.ella;

import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Java Class ArtistManager.
 * Manager of Artist results.
 * @author Harrington Joseph (Harph)
 * */
public class ArtistManager {
    /**
     * Extract all the data from the JSONArray and create an ArrayList of
     * Artist objects.
     * @param ellaConnection A connection to the Ella web service.
     * @param results The JSONArray that contains the WS response.
     * @return An ArrayList of Artist objects created from the JSONArray data.
     * */
    public final ArrayList<Artist> getArtists(
            final EllaConnection ellaConnection,
            final JSONArray results) {
        return this.getArtists(ellaConnection, results, null, null, true, null);
    }


    /**
     * Extract all the data from the JSONArray and create an ArrayList of
     * Artist objects.
     * @param ellaConnection A connection to the Ella web service.
     * @param results The JSONArray that contains the WS response.
     * @param mtd A String that contains the name of the type of search.
     * @param threshold The lowest Track score to consider. In case of null,
     * it will be considered as 0.
     * @param fuzzy A Boolean value that indicates if it was a fuzzy search.
     * @param metadataLinks name of the metalinks to obtain 
     * @return An ArrayList of Artist objects created from the JSONArray data.
     * */
    public final ArrayList<Artist> getArtists(
            final EllaConnection ellaConnection,
            final JSONArray results, final String mtd,
            Double threshold, final boolean fuzzy,
            final String[] metadataLinks) {
        ArrayList<Artist> artists = new ArrayList<Artist>();
        if (threshold == null) {
            threshold = 0.0;
        }
        String method = "";
        if (mtd != null) {
            method = mtd;
        }

        for (Object json : results) {
            JSONObject jsonArtist = (JSONObject) json;
            JSONObject jsonEntity = (JSONObject) jsonArtist.get("entity");
            JSONObject jsonMetadata = (JSONObject) jsonEntity.get("metadata");


            String artistId = (String) jsonEntity.get("id");
            if (artistId == null || artistId.trim().equals(""))
                continue;

            Object relevance = jsonArtist.get("score");
            if (relevance == null || relevance.toString().equals(""))
                continue;

            double score = new Double(relevance.toString());
            if (fuzzy && score < 0.4)
                continue;
            if (method.indexOf("resolve") != -1 && score < threshold)
                continue;
            else if (method.indexOf("match") != -1 && score < threshold)
                continue;

            String collection = (String) jsonEntity.get("collection");

            Artist artist = new Artist(ellaConnection, artistId, collection);
            artist.setName((String)jsonMetadata.get("name"));
            if (artist.getName() == null)
                continue;

            artist.setMbid((String)jsonMetadata.get("musicbrainz_artist_id"));

            Object apop = jsonMetadata.get("artist_popularity");
            double artistPopularity = apop != null && !apop.toString().equals("") ? new Double(apop.toString()) : 0.0;
            artist.setPopularity(artistPopularity);

            String artistLocation = (String) jsonMetadata.get("artist_location");
            artistLocation = artistLocation != null ? artistLocation : "";


            Object latlng = jsonMetadata.get("artist_latlng");
            if (latlng instanceof String)
                artist.setLatlng(latlng.toString().trim());
            else if (latlng instanceof JSONArray)
                artist.setLatlng((JSONArray)latlng);


            if (metadataLinks != null) {
                for (String link : metadataLinks) {
                    Object linkObject = jsonMetadata.get(link);
                    if (linkObject instanceof String)
                        artist.setLinks(link, (String)jsonMetadata.get(link));
                    else if (linkObject !=null)
                        artist.setLinks(link, (JSONArray) jsonMetadata.get(link));
                }
            }

            Object recommend = jsonMetadata.get("recommendable");
            artist.setRecommend(recommend);
            artists.add(artist);
        }
        return artists;
    }
}

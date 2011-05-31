package com.bmat.ella;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ArtistSearch extends Search{

	public ArtistSearch(EllaConnection ellaConnection, String method, String query, String collection, boolean fuzzy, Double threshold) {
		super(ellaConnection, collection);
		this.fuzzy = false;
		this.threshold = threshold != null ? threshold : 0.0;
	    this.searchTerms = new HashMap<String, String>();
		this.metadataLinks = new String[]{"official_homepage_artist_url","wikipedia_artist_url","lastfm_artist_url","myspace_artist_url","spotify_artist_url","discogs_artist_url"};
		this.metadata = "name,relevance,recommendable,artist_decades1,artist_decades2,artist_location,artist_latlng,artist_popularity,musicbrainz_artist_id," + Util.joinArray(this.metadataLinks, ",");
	    
	    String mtd = "/artists/";
		
		if(method.equals("resolve")){
			mtd += "resolve";
			searchTerms.put("artist", query);
			searchTerms.put("limit", "100");
			searchTerms.put("fetch_metadata", this.metadata);
		}
		else if(!this.fuzzy){// it's a search
			mtd += method.equals("search") || method.equals("match")? method : "search";
			searchTerms.put("q", query);
			searchTerms.put("limit", "100");
			searchTerms.put("fetch_metadata", this.metadata);
		}
		else{
			mtd += "match";
			searchTerms.put("q", query);
			searchTerms.put("limit", "30");
			searchTerms.put("fuzzy", "true");
			this.fuzzy = true;
		}
		searchTerms.put("fetch_metadata", this.metadata);
		this.method = mtd + this.RESPONSE_TYPE;
	}
	
	
	public ArrayList<Artist> getPage(long pageIndex) throws ServiceException, IOException {
		pageIndex = pageIndex > 0 ? pageIndex - 1 : 0;
		return this.getResults(this.retrievePage(pageIndex));
	}
	
	public ArrayList<Artist> getNextPage() throws ServiceException, IOException {
		return this.getResults(this.retrieveNextPage());
	} 
	
	private ArrayList<Artist> getResults(JSONArray jsonResults){
		if(jsonResults == null)
			return null;
		ArrayList<Artist> results = new ArrayList<Artist>();
		for(Object json : jsonResults){
			JSONObject jsonArtist = (JSONObject) json;
			JSONObject jsonEntity = (JSONObject) jsonArtist.get("entity");
			JSONObject jsonMetadata = (JSONObject) jsonEntity.get("metadata");
			
			
			String artistId = (String) jsonEntity.get("id");
			if(artistId == null || artistId.trim().equals(""))
				continue;
			
			Object relevance = jsonArtist.get("score");
			if(relevance == null || relevance.toString().equals(""))
				continue;
			
			double score = new Double(relevance.toString());
			if(this.fuzzy && score < 0.4)
				continue;
    		if(this.method.indexOf("resolve") != -1 && score < this.threshold)
    			continue;
    		else if(this.method.indexOf("match") != -1 && score < this.threshold)
    			continue;
			
			String collection = (String) jsonEntity.get("collection");
			
			Artist artist = new Artist(this.request.getEllaConnection(), artistId, collection);
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
			if(latlng instanceof String)
				artist.setLatlng(latlng.toString().trim());
			else if(latlng instanceof JSONArray)
				artist.setLatlng((JSONArray)latlng);
			
			
			
			for(String link : this.metadataLinks){
				Object linkObject = jsonMetadata.get(link);
				if(linkObject instanceof String)
					artist.setLinks(link, (String)jsonMetadata.get(link));
				else if(linkObject !=null)
					artist.setLinks(link, (JSONArray) jsonMetadata.get(link));
			}
			

			Object recommend = jsonMetadata.get("recommendable");
			artist.setRecommend(recommend);
			results.add(artist);
		}
		return results;
	}
	
}

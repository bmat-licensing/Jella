package com.bmat.ella;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class TrackSearch extends Search {

	/**
	 * method = search or match
	 * */
	public TrackSearch(EllaConnection ellaConnection, String method, String query, String collection, boolean fuzzy, Double threshold, String[] filter){
		super(ellaConnection, collection);
		this.fuzzy = false;
		this.threshold = threshold != null ? threshold : 0.0;
	    this.searchTerms = new HashMap<String, String>();
	    String mtd;
		if(!this.fuzzy){ // it's a search
			if(filter == null)
				this.searchTerms.put("q", "trackartist:" + query); 
			else{
				if(query == null)
					query = "";
				else if(!query.equals("") && filter.length > 0)
					query += " AND ";
				query += Util.joinArray(filter, " AND ");
				this.searchTerms.put("q", query);
			}
			searchTerms.put("limit", "10");
			mtd = method.equals("search") || method.equals("match")? method : "search";
		}
		else{
			this.searchTerms.put("q", query);
			this.searchTerms.put("limit", "30");
			this.searchTerms.put("fuzzy", "true");
			mtd = "match";
		}
		
		this.initialize("/tracks/" + mtd, collection);
		this.searchTerms.put("fetch_metadata", this.metadata);
	}
	
	/**
	 * method = resolve 
	 * */
	public TrackSearch(EllaConnection ellaConnection, HashMap<String, String> query, String collection, boolean fuzzy, Double threshold, String[] filter){
		super(ellaConnection, collection);
		this.initialize("/tracks/resolve", collection);
		this.fuzzy = false;
		this.threshold = threshold != null ? threshold : 0.0;
		String artist = (String)query.get("artist");
		String track = (String)query.get("track");
		
		this.searchTerms = new HashMap<String, String>();
	    if(artist != null)
	    	this.searchTerms.put("artist", artist);
	    if(track != null)
	    	this.searchTerms.put("track", track);
	    
	    this.searchTerms.put("limit", "100");
	    this.searchTerms.put("fetch_metadata", this.metadata);
	}
	
	private void initialize(String method, String collection){
		this.method = method + this.RESPONSE_TYPE;
		this.collection = collection;
		this.metadata = "track,artist_service_id,artist,release_service_id,release,location,year,genre,track_popularity,track_small_image,recommendable,musicbrainz_track_id,spotify_track_uri,";
	    this.metadataLinks = new String[]{"spotify_track_url", "grooveshark_track_url", "amazon_track_url","musicbrainz_track_url","hypem_track_url"};
	}
	
	public ArrayList<Track> getPage(Long pageIndex) throws Exception{
		if(pageIndex == null)
			pageIndex = new Long(0);
		return this.getResults(this.retrievePage(pageIndex));
	}
	
	private ArrayList<Track> getResults(JSONArray jsonResults){
		ArrayList<Track> results = new ArrayList<Track>();
		for(Object json : jsonResults){
			JSONObject jsonTrack = (JSONObject) json;
			JSONObject jsonEntity = (JSONObject) jsonTrack.get("entity");
			JSONObject jsonMetadata = (JSONObject) jsonEntity.get("metadata");
			String trackId = (String) jsonEntity.get("id");
			if(trackId == null || trackId.trim().equals(""))
				continue;
			
			String collection = (String) jsonEntity.get("collection");
			Object relevance = jsonTrack.get("score");
			double score = relevance != null && !relevance.toString().equals("")? new Double(relevance.toString()) : 0.0;
			
			if(this.fuzzy && score < 0.4)
				continue;
			
			if(this.method.indexOf("resolve") != -1 && score < this.threshold)
				continue;
			else if(this.method.indexOf("match") != -1 && score < this.threshold)
				continue;
			
			String artistId = (String)jsonMetadata.get("artist_service_id"); 
			if(artistId == null)
				continue;
			
			Artist artist = new Artist(this.request.getEllaConnection(), artistId, collection);
			artist.setName((String)jsonMetadata.get("artist"));
			if (artist.getName() == null)
				continue;
			Object apop = jsonEntity.get("artist_popularity");
			double artistPopularity = apop != null && !apop.toString().equals("") ? new Double(apop.toString()) : 0.0;
			artist.setPopularity(artistPopularity);
			
			String artistLocation = (String) jsonEntity.get("artist_location");
			artistLocation = artistLocation != null ? artistLocation : "";
			
			
			Object recommend = jsonEntity.get("recommendable");
			artist.setRecommend(recommend);
		
			Track track = new Track(this.request.getEllaConnection(), trackId, collection);
            track.setTitle((String)jsonMetadata.get("track"));
            track.setAudio((String)jsonMetadata.get("artist_location"));
            track.setArtist(artist);
            track.setMbid((String)jsonMetadata.get("musicbrainz_track_id"));
            track.setRecommend(recommend);
            
            Object tpop = jsonMetadata.get("track_popularity");
			double trackPopularity = tpop != null && !tpop.toString().equals("") ? new Double(tpop.toString()) : 0.0;
			artist.setPopularity(trackPopularity);
			
			for(String link : this.metadataLinks){
				track.setLink(link, (String)jsonMetadata.get(link));
			}
			Object trackSmallImages = jsonMetadata.get("track_small_image");
			if(trackSmallImages != null){
				if(trackSmallImages instanceof String){
					track.setImages(trackSmallImages.toString());
				}
				else{
					track.setImages((JSONArray)trackSmallImages);
				}
				
			}				
			
			
			Object albumId = jsonEntity.get("release_service_id");
			if(albumId != null){
				Album album = new Album(this.request.getEllaConnection(), (String) albumId, collection);
				album.setTitle((String)jsonMetadata.get("release"));
				album.setArtist(artist);
				album.setImage((String) jsonMetadata.get("release_small_image"));
				
				track.setAlbum(album);
			}
			results.add(track);
		}
		return results;
	}
}

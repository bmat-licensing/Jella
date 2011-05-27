package com.bmat.ella;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONArray;

public class Artist extends BaseObject{
	private String name;
	private String location;
	private ArrayList<HashMap<String, Double>> latlng;
	private Double popularity;
	
	public Artist(EllaConnection ellaConnection, String id, String collection){
		super(ellaConnection, id, collection);
		this.method = "/artists/" + this.id + ".json";
		
		this.metadataLinks = new String[]{"official_homepage_artist_url","wikipedia_artist_url","lastfm_artist_url","myspace_artist_url","spotify_artist_url","itms_artist_url","discogs_artist_url"};
		this.metadata = "artist,name,artist_popularity,artist_location,recommendable,artist_decades1,artist_decades2,artist_latlng,musicbrainz_artist_id,";
		this.metadata += Util.joinArray(this.metadataLinks, ",");
	}

	public String getName() {
		if(name == null){
			this.name = this.getFieldValue("name");
		}
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public ArrayList<HashMap<String, Double>> getLatlng() {
		if(this.latlng == null)
			this.latlng = new ArrayList<HashMap<String, Double>>();
		return latlng;
	}

	public void setLatlng(String latlng) {
		if(this.latlng == null)
			this.latlng = new ArrayList<HashMap<String, Double>>();
		
		if(!latlng.equals("")){
			String[] lat_lng = latlng.split(",");
			HashMap<String, Double> map = new HashMap<String, Double>();
			map.put("lat", new Double(lat_lng[0]));
			map.put("lng", new Double(lat_lng[1]));
			this.latlng.add(map);
		}
	}
	
	public void setLatlng(JSONArray latlngs) {
		for(int i = 0; i<latlngs.size(); i++)
			this.setLatlng(latlngs.get(i).toString());
	}
	
	public String getMbid(){
		if(this.mbid == null)
			this.setMbid(this.getFieldValue("musicbrainz_artist_id"));
		return this.mbid;
	}
	
	public double getPopularity(){
		if(this.popularity == null){
			String meta = this.getFieldValue("artist_popularity");
			if(meta != null)
				this.popularity = new Double(meta);
			else
				this.popularity = new Double(0);
		}
		return this.popularity;
	}
	
	public void setPopularity(double popularity) {
		this.popularity = popularity;
	}

}

package com.bmat.ella;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;

public class Artist extends BaseObject{
	private String name;
	private String location;
	private ArrayList<HashMap<String, Double>> latlng;
	
	public Artist(EllaConnection ellaConnection, String id, String collection){
		super(ellaConnection, id, collection);
	}

	public String getName() {
		return name;
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

}

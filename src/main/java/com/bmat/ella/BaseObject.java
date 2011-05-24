package com.bmat.ella;

import java.util.HashMap;

import org.json.simple.JSONArray;

public abstract class BaseObject extends SearchObject{
	
	protected String id;
	protected boolean recommend;
	protected double popularity;
	protected String mbid;
	protected HashMap<String, String[]> links;
	
	
	public BaseObject(EllaConnection ellaConnection, String id, String collection){
		super(ellaConnection, collection);
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isRecommend() {
		return recommend;
	}

	public void setRecommend(Object recommend) {
		if(recommend != null)
			this.recommend = new Boolean(recommend.toString().toLowerCase());
		else
			this.recommend = true;
	}

	public double getPopularity() {
		return popularity;
	}

	public void setPopularity(double popularity) {
		this.popularity = popularity;
	}

	public String getMbid() {
		return mbid;
	}


	public void setMbid(String mbid) {
		this.mbid = mbid;
	}
	
	public HashMap<String, String[]> getLinks() {
    	if(this.links == null){
    		this.links = new HashMap<String, String[]>();
    		try{
    			//TO-DO checkout pyella
    		}
    		catch(Exception e){
    			return this.links;
    		}
    	}
		return links;
	}
	
	public void setLinks(String service, JSONArray links) {
		if(this.links == null)
			this.links = new HashMap<String, String[]>();
		String[] serviceLinks = new String[links.size()]; 
		for(int i =0; i<serviceLinks.length; i++)
			serviceLinks[i] = links.get(i).toString();
		this.links.put(service, serviceLinks);
	}

	public void setLinks(String service, String link) {
		if(this.links == null)
			this.links = new HashMap<String, String[]>();
		this.links.put(service, new String[]{link});
	}
	
}

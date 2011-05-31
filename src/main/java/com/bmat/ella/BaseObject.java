package com.bmat.ella;

import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public abstract class BaseObject extends SearchObject{
	
	protected String id;
	protected Boolean recommend;
	
	protected String mbid;
	protected HashMap<String, String[]> links;
	protected JSONObject json;
	
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

	public Boolean isRecommend() {
		if(this.recommend == null)
			this.setRecommend(this.getFieldValue("recommendable"));
		return recommend;
	}

	public void setRecommend(Object recommend) {
		if(recommend != null)
			this.recommend = new Boolean(recommend.toString().toLowerCase());
		else
			this.recommend = true;
	}

	public String getMbid() {
		return mbid;
	}
	
	public void setMbid(String mbid) {
		if(mbid != null)
			this.mbid = mbid;
		else
			this.mbid = "";
	}
	
	public HashMap<String, String[]> getLinks() {
    	if(this.links == null){
    		this.links = new HashMap<String, String[]>();
    		try{
    			if(this.json == null){
    				HashMap<String, String> fetchMetadata = new HashMap<String, String>();
    				fetchMetadata.put("fetch_metadata", this.metadata);
    				JSONArray response = (JSONArray)this.request(fetchMetadata);
    					
    				this.json = (JSONObject) response.get(0);
    			}
    			JSONObject jsonMetadata = (JSONObject) this.json.get("metadata");
				for(String link : this.metadataLinks){
					Object linkObject = jsonMetadata.get(link);
					if(linkObject instanceof String)
						this.setLinks(link, (String)jsonMetadata.get(link).toString());
					else if(linkObject !=null)
						this.setLinks(link, (JSONArray) jsonMetadata.get(link));
				}
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

	public JSONObject getJson() {
		return json;
	}

	public void setJson(JSONObject json) {
		this.json = json;
	}
	
	
	private void obtainJson(){
		if(this.json == null){
			try{
				HashMap<String, String> fetchMetadata = new HashMap<String, String>();
				fetchMetadata.put("fetch_metadata", this.metadata);
				JSONArray response = (JSONArray)this.request(fetchMetadata);
				this.json = (JSONObject) response.get(0);
			}
			catch(Exception e){
				this.json = null;
			}
		}
	}
	
	protected String getFieldValue(String name){
		String fieldValue = null;
		this.obtainJson();
		JSONObject jsonMetadata = (JSONObject) this.json.get("metadata");
		Object objValue = jsonMetadata.get(name);
		fieldValue = objValue != null ? objValue.toString() : null;
		return fieldValue;
	}
	
	protected JSONArray getFieldValues(String name){
		this.obtainJson();
		JSONObject jsonMetadata = (JSONObject) this.json.get("metadata");
		JSONArray objValue = (JSONArray) jsonMetadata.get(name);
		return objValue;
	}
}

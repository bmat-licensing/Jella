package com.bmat.ella;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public abstract class Search extends SearchObject {
	
	protected final String RESPONSE_TYPE = ".json";
	private int RESULTS_PER_PAGE = 10;
	protected boolean fuzzy = false;
    protected HashMap<String, String> searchTerms;
    protected Double threshold;
    private long lastPageIndex;
    private Long hits;
    
	
	public Search(EllaConnection ellaConnection, String collection){
		super(ellaConnection, collection);
		this.lastPageIndex = -1;
		this.hits = null;
	}
	
	@SuppressWarnings("rawtypes")
	public abstract ArrayList getPage(long pageIndex) throws ServiceException, IOException;
	
	@SuppressWarnings("rawtypes")
	public abstract ArrayList getNextPage() throws ServiceException, IOException;

	protected JSONArray retrievePage(long pageIndex) throws ServiceException, IOException{		
		HashMap<String, String> params = this.getParams();
		if(this.method.indexOf("resolve") == -1){
			long offset = 0;
			if(pageIndex !=0)
				offset = this.RESULTS_PER_PAGE * pageIndex;
			params.put("offset", Long.toString(offset));
		}
		
		JSONObject response = (JSONObject) this.request(params);
		return response != null ? (JSONArray)response.get("results") : null;
	}
	
	protected JSONArray retrieveNextPage() throws ServiceException, IOException{
		this.lastPageIndex += 1;
		return this.retrievePage(this.lastPageIndex);
	}
	
	public long getTotalResultCount() throws ServiceException, IOException{
		if(this.hits != null)
			return this.hits;
		
		HashMap<String, String> params = this.getParams();
		if(this.method.indexOf("resolve") == -1){
			params.put("offset", "0");
			params.put("fetch_metadata", "_none");
		}
		else
			params.remove("fetch_metadata");

		params.put("limit", "1");		
		JSONObject response = (JSONObject) this.request(params);
		JSONObject stats = (JSONObject) response.get("stats");
		long totalHits;
		if(this.collection != null)
			totalHits = new Long(stats.get("total_hits").toString());
		else{
			totalHits = 0;
			JSONObject jsonHits = (JSONObject) stats.get("total_hits");
			for(Object key:jsonHits.keySet()){
				totalHits += new Long(jsonHits.get(key.toString()).toString());
			}
		}
		this.hits = totalHits;
		return this.hits;	
	}
	
	private HashMap<String, String> getParams(){
		HashMap<String, String> params = new HashMap<String, String>();
		for(String key:this.searchTerms.keySet())
			params.put(key, this.searchTerms.get(key).toString());
		return params;
	}
	
}

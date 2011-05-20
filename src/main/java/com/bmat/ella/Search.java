package com.bmat.ella;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public abstract class Search {
	
	protected Request request;
	
	protected final String RESPONSE_TYPE = ".json";
	private int RESULTS_PER_PAGE = 10;
	
	protected String method;
	protected String collection;
	protected boolean fuzzy = false;
	protected String metadata;
    protected String[] metadataLinks;
    protected HashMap<String, String> searchTerms;
    protected Double threshold;
    
	
	public Search(EllaConnection ellaConnection){
		request = new Request(ellaConnection);
	}

	public JSONArray retrievePage(long pageIndex) throws Exception{		
		if(this.method.indexOf("resolve") == -1){
			long offset = 0;
			if(pageIndex !=0)
				offset = this.RESULTS_PER_PAGE * pageIndex;
			this.searchTerms.put("offset", Long.toString(offset));
		}
		
		JSONObject response = this.request.execute(this.method, this.collection, this.searchTerms);
		return response != null ? (JSONArray)response.get("results") : new JSONArray();
		
	}
	
	public void getNextPage(){
		
	}
	
	public void getTotalResultCount(){
		
	}
	
	
	@SuppressWarnings("rawtypes")
	public abstract ArrayList getPage(Long pageIndex) throws Exception;
	
	
	
//	public abstract void getNextPage();
//	
	
}

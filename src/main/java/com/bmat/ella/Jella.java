package com.bmat.ella;

import java.util.ArrayList;
import java.util.HashMap;

public class Jella {

  private String ellaws;

	private EllaConnection ellaConnection;
	
	public Jella(String ellaws, String username, String password){
		this.ellaws = ellaws;
		this.ellaConnection = new EllaConnection(this.ellaws, username, password);
	}
	
	public ArrayList<Track> searchTrack(String method, String query, String collection, boolean fuzzy, Double threshold, String[] filter, Long page) throws Exception{
		return new TrackSearch(ellaConnection, method, query, collection, fuzzy, threshold, filter).getPage(page);
	}
	
	public ArrayList<Track> searchTrack(String method, HashMap<String, String> query, String collection, boolean fuzzy, Double threshold, String[] filter, Long page) throws Exception{
		return new TrackSearch(ellaConnection, query, collection, fuzzy, threshold, filter).getPage(page);
	}
}

package com.bmat.ella;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Jella {

	private String ellaws;
	private EllaConnection ellaConnection;
	
	public Jella(String ellaws, String username, String password){
		this.ellaws = ellaws;
		this.ellaConnection = new EllaConnection(this.ellaws, username, password);
	}
	
	public ArrayList<Track> searchTracks(String method, String query, String collection, boolean fuzzy, Double threshold, String[] filter, Long page) throws ServiceException, IOException{
		return new TrackSearch(this.ellaConnection, method, query, collection, fuzzy, threshold, filter).getPage(page);
	}
	
	/* method = resolve */
	public ArrayList<Track> searchTracks(HashMap<String, String> query, String collection, boolean fuzzy, Double threshold, String[] filter, Long page) throws ServiceException, IOException {
		return new TrackSearch(this.ellaConnection, query, collection, fuzzy, threshold, filter).getPage(page);
	}
}

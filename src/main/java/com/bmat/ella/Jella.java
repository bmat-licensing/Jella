package com.bmat.ella;

import java.io.IOException;
import java.util.HashMap;

public class Jella {

	private EllaConnection ellaConnection;
	
	public Jella(String ellaws, String username, String password){
		this.ellaConnection = new EllaConnection(ellaws, username, password);
	}

	public EllaConnection getEllaConnection() {
		return ellaConnection;
	}

	public void setEllaConnection(EllaConnection ellaConnection) {
		this.ellaConnection = ellaConnection;
	}

	public TrackSearch searchTracks(String method, String query, String collection, boolean fuzzy, Double threshold, String[] filter) throws ServiceException, IOException{
		return new TrackSearch(this.ellaConnection, method, query, collection, fuzzy, threshold, filter);
	}
	
	/* method = resolve */
	public TrackSearch searchTracks(HashMap<String, String> query, String collection, boolean fuzzy, Double threshold, String[] filter) throws ServiceException, IOException {
		return new TrackSearch(this.ellaConnection, query, collection, fuzzy, threshold, filter);
	}
	
	public ArtistSearch searchArtists(String method, String query, String collection, boolean fuzzy, Double threshold) throws Exception{
		return new ArtistSearch(this.ellaConnection, method, query, collection, fuzzy, threshold);
	}
}

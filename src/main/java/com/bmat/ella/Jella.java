package com.bmat.ella;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;

public class Jella {
	private static String ellaws = "http://ella.bmat.ws"; // DEFAULT URL to web server access
	private EllaConnection ellaConnection;

	
	public Jella(String username, String password){
		this.ellaConnection = new EllaConnection(this.ellaws, username, password);
	}
	
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

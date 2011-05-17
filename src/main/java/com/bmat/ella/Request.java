package com.bmat.ella;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class Request {
	
	private String ellaws;
	
	public Request(String ellaws, String username, String password){
		this.ellaws = fixHostname(ellaws);
		Authenticator.setDefault(new EllaAuthenticator(username, password));
	}
	
	private String fixHostname(String hostname){
		if(!hostname.startsWith("http://")){
			hostname = "http://" + hostname;
		}
		if(!hostname.endsWith("/")){
			hostname += "/";
		}
		return hostname;
	}
	
	public void execute(String method, String collection, HashMap<String, String> searchTerms){
		try {
			this.downloadResponse(method, collection, searchTerms);
		} catch (IOException e) {
			//get errors
		}
	}
	
	private String downloadResponse(String method, String collection, HashMap<String, String> searchTerms) throws IOException{
		String params = "";
		String sep = "";
		for(String key : searchTerms.keySet()){
			params += sep + key + "=" + URLEncoder.encode(searchTerms.get(key), "utf-8"); 
			sep = "&";
		}
		
		String url = this.ellaws + "collections/" + collection + method + "?" + params;
		System.out.println("url: " + url);
		
		
		URLConnection urlCon = new URL(url).openConnection();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
		String inputLine, jsonResponse = "";
		while((inputLine = bufferedReader.readLine()) != null){
			System.out.println(inputLine);
			jsonResponse += inputLine;
		}
		bufferedReader.close();
		return jsonResponse;
	}
}

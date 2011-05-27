package com.bmat.ella;

import java.net.Authenticator;

public class EllaConnection {
	private String ellaws;
	
	public EllaConnection(String ellaws, String username, String password){
		this.ellaws = fixHostname(ellaws);
		if (username != null && password != null) {
			Authenticator.setDefault(new EllaAuthenticator(username, password));
		}
	}
	
	private String fixHostname(String hostname){
		if(!hostname.startsWith("http://")){
			hostname = "http://" + hostname;
		}
		if(hostname.endsWith("/")){
			hostname += hostname.substring(0, hostname.length()-1);
		}
		return hostname;
	}
	
	public String getEllaws(){
		return this.ellaws;
	}
}

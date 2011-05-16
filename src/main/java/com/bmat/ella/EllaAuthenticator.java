package com.bmat.ella;
import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class EllaAuthenticator extends Authenticator {
	private String username;
	private String password;
	
	public EllaAuthenticator(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	protected PasswordAuthentication getPasswordAuthentication(){
		return new PasswordAuthentication(this.username, this.password.toCharArray());
	}
	
}

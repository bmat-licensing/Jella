package com.bmat.ella;

public class ServiceException extends Exception{
	
	private static final long serialVersionUID = 1L;
	private String type;
	private String message;
	
	public ServiceException(String type, String message){
		super();
		this.type = type;
		this.message = message;
	}
	
	public String getType(){
		return this.type;
	}
	
	public String getMessage(){
		return this.message;
	}
	
	public String toString(){
		return this.type + ": " + this.message;
	}
}

package com.bmat.ella;

public class Artist extends BaseObject{
	private String name;
	private String location;
	
	
	public Artist(EllaConnection ellaConnection, String id, String collection){
		super(ellaConnection, id, collection);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
}

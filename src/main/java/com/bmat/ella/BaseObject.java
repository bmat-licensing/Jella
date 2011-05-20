package com.bmat.ella;

public abstract class BaseObject {
	protected EllaConnection ellaConnection;
	protected String id;
	protected String collection;
	protected boolean recommend;
	protected double popularity;
	
	public BaseObject(EllaConnection ellaConnection, String id, String collection){
		this.ellaConnection = ellaConnection;
		this.id = id;
		this.collection = collection;
	}

	public EllaConnection getEllaConnection() {
		return ellaConnection;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}


	public boolean isRecommend() {
		return recommend;
	}

	public void setRecommend(Object recommend) {
		if(recommend != null)
			this.recommend = new Boolean(recommend.toString().toLowerCase());
		else
			this.recommend = true;
	}

	public void setEllaConnection(EllaConnection ellaConnection) {
		this.ellaConnection = ellaConnection;
	}

	public double getPopularity() {
		return popularity;
	}

	public void setPopularity(double popularity) {
		this.popularity = popularity;
	}
}

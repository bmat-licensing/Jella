package com.bmat.ella;

public abstract class BaseObject extends SearchObject{
	
	protected String id;
	protected boolean recommend;
	protected double popularity;
	
	
	public BaseObject(EllaConnection ellaConnection, String id, String collection){
		super(ellaConnection, collection);
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public double getPopularity() {
		return popularity;
	}

	public void setPopularity(double popularity) {
		this.popularity = popularity;
	}

}

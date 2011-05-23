package com.bmat.ella;

public abstract class SearchObject {
	protected EllaConnection ellaConnection;
	protected String collection;
	protected String metadata;
    protected String[] metadataLinks;
    
    public SearchObject(EllaConnection ellaConnection, String collection){
    	this.ellaConnection = ellaConnection;
    	this.collection = collection;
    }
    
    public EllaConnection getEllaConnection() {
		return ellaConnection;
	}

	public void setEllaConnection(EllaConnection ellaConnection) {
		this.ellaConnection = ellaConnection;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

	public String[] getMetadataLinks() {
		return metadataLinks;
	}

	public void setMetadataLinks(String[] metadataLinks) {
		this.metadataLinks = metadataLinks;
	}
}

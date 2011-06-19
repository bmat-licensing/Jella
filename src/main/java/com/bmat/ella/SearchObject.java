package com.bmat.ella;

import java.io.IOException;
import java.util.HashMap;


public abstract class SearchObject {
    protected final String RESPONSE_TYPE = ".json";
    protected Request request;
    protected String method;
    protected String collection;
    protected String metadata;
    protected String[] metadataLinks;

    public SearchObject(EllaConnection ellaConnection, String collection){
        this.request = new Request(ellaConnection);
        this.collection = collection;
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object request(String method, String collection, HashMap<String, String> params) throws ServiceException, IOException{
        return this.request.execute(method, collection, params, true);
    }

    public Object request(String method, HashMap<String, String> params) throws ServiceException, IOException{
        return this.request(method, this.collection, params);
    }

    public Object request(HashMap<String, String> params) throws ServiceException, IOException{
        return this.request(this.method, params);
    }
}

package com.bmat.ella;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Java Class SearchObject.
 * Represents a search object.
 * @author Harrington Joseph (Harph)
 * */
public class SearchObject {
    /**
     * Format of request response.
     * */
    protected static final String RESPONSE_TYPE = ".json";
    /**
     * Request object associated.
     * */
    protected Request request;
    /**
     * Kind of search.
     * */
    protected String method;
    /**
     * Collection name.
     * */
    protected String collection;
    /**
     * Search metadata.
     * */
    protected String metadata;
    /**
     * Search metadata links.
     * */
    protected String[] metadataLinks;

    /**
     * Class constructor.
     * @param ellaConnection A connection to the Ella web service.
     * @param collectionValue The collection name of the album.
     * */
    public SearchObject(final EllaConnection ellaConnection,
            final String collectionValue) {
        this.request = new Request(ellaConnection);
        this.collection = collectionValue;
    }

    /**
     * @return The collection name.
     * */
    public final String getCollection() {
        return collection;
    }

    /**
     * Sets the collection name.
     * @param collectionValue The collection name.
     * */
    public final void setCollection(final String collectionValue) {
        this.collection = collectionValue;
    }

    /**
     * @return The search metadata.
     * */
    public final String getMetadata() {
        return metadata;
    }

    /**
     * Sets the object metadata.
     * @param metadataValues The metadata values.
     * */
    public final void setMetadata(final String metadataValues) {
        this.metadata = metadataValues;
    }

    /**
     * @return The search metadata links.
     * */
    public final String[] getMetadataLinks() {
        if (this.metadataLinks == null) {
            return null;
        }
        return Arrays.copyOf(this.metadataLinks, this.metadataLinks.length);
    }

    /**
     * Sets the search method.
     * @param metadataLinkValues The metadata links.
     * */
    public final void setMetadataLinks(final String[] metadataLinkValues) {
        if (metadataLinkValues != null) {
            this.metadataLinks = Arrays.copyOf(metadataLinkValues,
                metadataLinkValues.length);
        } else {
            this.metadataLinks = null;
        }
    }

    /**
     * @return The search method.
     * */
    public final String getMethod() {
        return method;
    }

    /**
     * Sets the search method.
     * @param methodValue The search method.
     * */
    public final void setMethod(final String methodValue) {
        this.method = methodValue;
    }

    /**
     * Execute the search request.
     * @param methodName The search method type.
     * @param collectionName The search collection name.
     * @param params The search params.
     * @return An object with the response value.
     * @throws ServiceException When there WS fails.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * */
    public final Object request(final String methodName,
            final String collectionName, final HashMap<String, String> params)
    throws ServiceException, IOException {
        return this.request.execute(methodName, collectionName, params, true);
    }

    /**
     * Execute the search request.
     * @param methodName The search kind type.
     * @param params The search params.
     * @return An object with the response value.
     * @throws ServiceException When there WS fails.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * */
    public final Object request(final String methodName,
            final HashMap<String, String> params)
    throws ServiceException, IOException {
        return this.request(methodName, this.collection, params);
    }

    /**
     * Execute the search request.
     * @param params The search params.
     * @return An object with the response value.
     * @throws ServiceException When there WS fails.
     * @throws IOException When there is a problem with the
     * connection to Ella WS.
     * */
    public final Object request(final HashMap<String, String> params)
    throws ServiceException, IOException {
        return this.request(this.method, params);
    }
}

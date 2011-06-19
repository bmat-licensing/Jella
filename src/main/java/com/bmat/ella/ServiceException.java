
package com.bmat.ella;
/**
 * Java Class ServiceException.
 * Represents an Ella Service Exception.
 * @author Harrington Joseph (Harph)
 * */
public class ServiceException extends Exception {
    /**
     * Serialize version ID.
     * */
    private static final long serialVersionUID = 1L;
    /**
     * Service exception type.
     * */
    private String type;
    /**
     * Service exception message.
     * */
    private String message;

    /**
     * Class constructor.
     * @param typeValue Service exception type.
     * @param messageValue Service exception message.
     * */
    public ServiceException(final String typeValue, final String messageValue) {
        super();
        this.type = typeValue;
        this.message = messageValue;
    }

    /**
     * @return A String with the service exception type.
     * */
    public final String getType() {
        return this.type;
    }

    /**
     * @return A String with the service exception message.
     * */
    public final String getMessage() {
        return this.message;
    }

    /**
     * @return A string that represents the exception.
     * */
    public final String toString() {
        return this.type + ": " + this.message;
    }
}

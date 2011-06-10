
package com.bmat.ella;

public class ServiceException extends Exception {
    private static final long serialVersionUID = 1L;
    private String type;
    private String message;

    public ServiceException(String typeValue, String messageValue) {
        super();
        this.type = typeValue;
        this.message = messageValue;
    }

    public final String getType() {
        return this.type;
    }

    public final String getMessage() {
        return this.message;
    }

    public final String toString() {
        return this.type + ": " + this.message;
    }
}


package com.bmat.ella;

import java.net.Authenticator;
/**
 * Java Class EllaConnection.
 * Represents a Connection to Ella WS.
 * @author Harrington Joseph (Harph)
 * */
public class EllaConnection {
    /**
     * A String that contains the URL to Ella WS.
     * */
    private String ellaws;

    /**
     * Class constructor.
     * @param ellawsURL The URL to Ella WS.
     * @param username A String that represents the username.
     * @param password A String that represents the password.
     * */
    public EllaConnection(final String ellawsURL,
            final String username, final String password) {
        this.ellaws = fixHostname(ellawsURL);
        if (username != null && password != null) {
            Authenticator.setDefault(new EllaAuthenticator(username,
                    password));
        }
    }

    /**
     * Checks if the URL container "http://" and remove the last "/".
     * @param hostnameValue to fix.
     * @return a String with the URL fixed.
     * */
    private String fixHostname(final String hostnameValue) {
        String hostname = hostnameValue;
        if (!hostname.startsWith("http://")) {
            hostname = "http://" + hostname;
        }
        if (hostname.endsWith("/")) {
            hostname += hostname.substring(0, hostname.length() - 1);
        }
        return hostname;
    }

    /**
     * @return a String with the URL to Ella WS.
     * */
    public final String getEllaws() {
        return this.ellaws;
    }
}


package com.bmat.ella;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
/**
 * Java Class EllaAuthenticator.
 * Represents an Ella authentication.
 * @author Harrington Joseph (Harph)
 * */
public class EllaAuthenticator extends Authenticator {
    /**
     * A String that represents the username.
     * */
    private String username;
    /**
     * A String that represents the password.
     * */
    private String password;

    /**
     * Class constructor.
     * @param username A String that represents the username.
     * @param password A String that represents the password.
     * */
    public EllaAuthenticator(final String usernameValue, final String passwordValue) {
        this.username = usernameValue;
        this.password = passwordValue;
    }

    /**
     * @return a java.net.PasswordAuthentication.PasswordAuthentication.
     * */
    public final PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(this.username,
                this.password.toCharArray());
    }
}

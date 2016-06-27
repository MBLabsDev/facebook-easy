package br.com.mblabs.facebookeasy;

public class FacebookManagerException extends Exception {

    public FacebookManagerException() {
        this("");
    }

    public FacebookManagerException(final String detailMessage) {
        super("Error facebook manager class: " + detailMessage);
    }
}

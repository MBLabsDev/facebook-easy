package br.com.mblabs.facebookeasy.data.dao;

public class FacebookDAOException extends Exception {

    public FacebookDAOException() {
        this("");
    }

    public FacebookDAOException(final String detailMessage) {
        super("Error instantiating a facebook dao class: " + detailMessage);
    }
}

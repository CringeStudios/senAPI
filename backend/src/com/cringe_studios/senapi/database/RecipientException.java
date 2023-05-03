package com.cringe_studios.senapi.database;

public class RecipientException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public RecipientException () {

    }

    public RecipientException (String message) {
        super (message);
    }

    public RecipientException (Throwable cause) {
        super (cause);
    }

    public RecipientException (String message, Throwable cause) {
        super (message, cause);
    }
}

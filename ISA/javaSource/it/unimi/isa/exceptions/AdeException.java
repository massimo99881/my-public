package it.unimi.isa.exceptions;

public class AdeException extends Exception {

	private static final long serialVersionUID = -8309369086190788419L;

	public AdeException() {
		super();
	}

	public AdeException(String message) {
		super(message);
	}

	public AdeException(String message, Throwable cause) {
		super(message, cause);
	}

	public AdeException(Throwable cause) {
		super(cause);
	}

}

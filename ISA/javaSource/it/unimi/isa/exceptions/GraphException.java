package it.unimi.isa.exceptions;

public class GraphException extends Exception {
	
	private static final long serialVersionUID = 503725753371461080L;

	public GraphException() {
		super();
	}

	public GraphException(String message) {
		super(message);
	}

	public GraphException(String message, Throwable cause) {
		super(message, cause);
	}

	public GraphException(Throwable cause) {
		super(cause);
	}
}

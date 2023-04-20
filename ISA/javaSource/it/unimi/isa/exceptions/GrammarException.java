package it.unimi.isa.exceptions;

public class GrammarException extends Exception {

	private static final long serialVersionUID = 438902824325387654L;

	public GrammarException() {
		super();
	}

	public GrammarException(String message) {
		super(message);
	}

	public GrammarException(String message, Throwable cause) {
		super(message, cause);
	}

	public GrammarException(Throwable cause) {
		super(cause);
	}
}

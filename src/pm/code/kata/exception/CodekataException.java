package pm.code.kata.exception;

public class CodekataException extends Exception {

	private static final long serialVersionUID = 1L;

	public CodekataException(String message) {
		super(message);
	}

	public CodekataException(String message, Throwable cause) {
		super(message, cause);
	}
}

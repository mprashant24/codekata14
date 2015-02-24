package pm.code.kata.exception;

public class BookReaderException extends CodekataException {

	private static final long serialVersionUID = 1L;

	public BookReaderException(String message) {
		super(message);
	}

	public BookReaderException(String message, Throwable cause) {
		super(message, cause);
	}

}

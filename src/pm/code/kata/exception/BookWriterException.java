package pm.code.kata.exception;

public class BookWriterException extends CodekataException {

	private static final long serialVersionUID = 1L;

	public BookWriterException(String message) {
		super(message);
	}

	public BookWriterException(String message, Throwable cause) {
		super(message, cause);
	}

}

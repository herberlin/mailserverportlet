package de.herberlin.wwwutil.httperror;

/**
 * @author hans joachim herbertz
 * created 27.12.2003
 */
public class NotImplemented_501 extends InternalServerError_500 {

	/**
	 * 
	 */
	public NotImplemented_501() {
		super();
	}

	/**
	 * @param message
	 */
	public NotImplemented_501(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NotImplemented_501(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public NotImplemented_501(Throwable cause) {
		super(cause);
	}


	/**
	 * @see HttpError#getReason()
	 */
	protected String getReason() {
		return "501 Not Implemented";
	}

}

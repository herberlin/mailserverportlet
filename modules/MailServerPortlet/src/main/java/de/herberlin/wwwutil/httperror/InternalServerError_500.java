package de.herberlin.wwwutil.httperror;

/**
 *
 * @author Hans Joachim Herbertz
 * @created 01.02.2003
 */
public class InternalServerError_500 extends HttpError {

	/**
	 * Constructor for InternalServerError_500.
	 */
	public InternalServerError_500() {
		super();
	}

	/**
	 * Constructor for InternalServerError_500.
	 * @param message
	 */
	public InternalServerError_500(String message) {
		super(message);
	}

	/**
	 * Constructor for InternalServerError_500.
	 * @param message
	 * @param cause
	 */
	public InternalServerError_500(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor for InternalServerError_500.
	 * @param cause
	 */
	public InternalServerError_500(Throwable cause) {
		super(cause);
	}

	/**
	 * @see HttpError#getReason()
	 */
	protected String getReason() {
		return "500 Internal Server Error";
	}

	/**
	 * @see HttpError#getMoreHeaders()
	 */
	protected String getMoreHeaders() {
		return null;
	}

}

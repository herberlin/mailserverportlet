package de.herberlin.wwwutil.httperror;

/**
 *
 * @author Hans Joachim Herbertz
 * @created 31.01.2003
 */
public class HttpVersion_505 extends HttpError {

	/**
	 * Constructor for HttpVersion_505.
	 */
	public HttpVersion_505() {
		super();
	}

	/**
	 * Constructor for HttpVersion_505.
	 * @param message
	 */
	public HttpVersion_505(String message) {
		super(message);
	}

	/**
	 * Constructor for HttpVersion_505.
	 * @param message
	 * @param cause
	 */
	public HttpVersion_505(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor for HttpVersion_505.
	 * @param cause
	 */
	public HttpVersion_505(Throwable cause) {
		super(cause);
	}

	/**
	 * @see HttpError#getReason()
	 */
	protected String getReason() {
		return "505 HTTP Version Not Supported" ;
	}

	/**
	 * @see HttpError#getMoreHeaders()
	 */
	protected String getMoreHeaders() {
		return null;
	}

}

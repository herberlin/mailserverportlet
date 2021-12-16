package de.herberlin.wwwutil.httperror;

/**
 *
 * @author Hans Joachim Herbertz
 * @created 31.01.2003
 */
public class ConnectionLost extends HttpError {

	/**
	 * Constructor for ConnectionLost.
	 */
	public ConnectionLost() {
		super();
	}

	/**
	 * Constructor for ConnectionLost.
	 * @param message
	 */
	public ConnectionLost(String message) {
		super(message);
	}

	/**
	 * Constructor for ConnectionLost.
	 * @param message
	 * @param cause
	 */
	public ConnectionLost(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor for ConnectionLost.
	 * @param cause
	 */
	public ConnectionLost(Throwable cause) {
		super(cause);
	}

	/**
	 * @see HttpError#getReason()
	 */
	protected String getReason() {
		return "0 Connection Lost.";
	}

	/**
	 * @see HttpError#getMoreHeaders()
	 */
	protected String getMoreHeaders() {
		return null;
	}

}

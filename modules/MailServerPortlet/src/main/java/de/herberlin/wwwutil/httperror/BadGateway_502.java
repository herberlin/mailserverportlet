package de.herberlin.wwwutil.httperror;

/**
 *
 * @author Hans Joachim Herbertz
 * @created 01.02.2003
 */
public class BadGateway_502 extends InternalServerError_500 {

	/**
	 * Constructor for BadGateway_502.
	 */
	public BadGateway_502() {
		super();
	}

	/**
	 * Constructor for BadGateway_502.
	 * @param message
	 */
	public BadGateway_502(String message) {
		super(message);
	}

	/**
	 * Constructor for BadGateway_502.
	 * @param message
	 * @param cause
	 */
	public BadGateway_502(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor for BadGateway_502.
	 * @param cause
	 */
	public BadGateway_502(Throwable cause) {
		super(cause);
	}
	protected String getReason() {
		return "502 Bad Gateway";
	}
}

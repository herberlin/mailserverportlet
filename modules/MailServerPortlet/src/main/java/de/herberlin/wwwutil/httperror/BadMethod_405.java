package de.herberlin.wwwutil.httperror;

/**
 *
 * @author Hans Joachim Herbertz
 * @created 31.01.2003
 */
public class BadMethod_405 extends BadRequest_400 {

	/**
	 * Constructor for BadMethod_405.
	 */
	public BadMethod_405() {
		super();
	}

	/**
	 * Constructor for BadMethod_405.
	 * @param message
	 */
	public BadMethod_405(String message) {
		super(message);
	}

	/**
	 * Constructor for BadMethod_405.
	 * @param message
	 * @param cause
	 */
	public BadMethod_405(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor for BadMethod_405.
	 * @param cause
	 */
	public BadMethod_405(Throwable cause) {
		super(cause);
	}
	
	protected String getReason() {
		return "405 Method Not Allowed";
	}

}

package de.herberlin.wwwutil.httperror;

/**
 *
 * @author Hans Joachim Herbertz
 * @created 01.02.2003
 */
public class LengthRequired_411 extends BadRequest_400 {

	/**
	 * Constructor for LengthRequired_411.
	 */
	public LengthRequired_411() {
		super();
	}

	/**
	 * Constructor for LengthRequired_411.
	 * @param message
	 */
	public LengthRequired_411(String message) {
		super(message);
	}

	/**
	 * Constructor for LengthRequired_411.
	 * @param message
	 * @param cause
	 */
	public LengthRequired_411(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor for LengthRequired_411.
	 * @param cause
	 */
	public LengthRequired_411(Throwable cause) {
		super(cause);
	}
	
	protected String getReason() {
		return "411 Length Required";	
	}
}

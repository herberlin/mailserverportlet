package de.herberlin.wwwutil.httperror;

/**
 * @author hans joachim herbertz
 * created 28.12.2003
 */
public class NotFound_404 extends BadRequest_400 {

	/**
	 * 
	 */
	public NotFound_404() {
		super();
	}

	/**
	 * @param message
	 */
	public NotFound_404(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NotFound_404(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public NotFound_404(Throwable cause) {
		super(cause);
	}

	/**
	 * @see HttpError#getReason()
	 */
	protected String getReason() {
		return "404 Not Found";
	}

}

package de.herberlin.wwwutil.httperror;

/**
 * @author hans joachim herbertz
 * created 28.12.2003
 */
public class MovedTemporarily_302 extends HttpError {


	private String location=null;
	/**
	 * @param message
	 */
	public MovedTemporarily_302(String location) {
		super("Moved Temporarily");
		this.location=location;
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MovedTemporarily_302(String location, Throwable cause) {
		super("Moved Temporarily", cause);
		this.location=location;
	}

	/**
	 * @see HttpError#getReason()
	 */
	protected String getReason() {
		return "302 Moved Temporarily<br>Location: <a href=\"XXX\">XXX</a>".replaceAll("XXX",location);
	}

	/**
	 * @see HttpError#getMoreHeaders()
	 */
	protected String getMoreHeaders() {
		return "Location: "+location;
	}

}

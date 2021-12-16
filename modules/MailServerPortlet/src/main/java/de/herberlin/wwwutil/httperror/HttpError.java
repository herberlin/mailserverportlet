package de.herberlin.wwwutil.httperror;

import java.io.OutputStream;

/**
 *
 * @author Hans Joachim Herbertz
 * @created 30.01.2003
 */
public abstract class HttpError extends Exception {

	//public static final String CR = new String(new byte[] { 10, 13 });
	public static final String CR = "\n";

	/**
	 * Constructor for HttpError.
	 */
	public HttpError() {
		super();
	}

	/**
	 * Constructor for HttpError.
	 * @param message
	 */
	public HttpError(String message) {
		super(message);
	}

	/**
	 * Constructor for HttpError.
	 * @param message
	 * @param cause
	 */
	public HttpError(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor for HttpError.
	 * @param cause
	 */
	public HttpError(Throwable cause) {
		super(cause);
	}
	/**
	 * Returns the reason message in the form e.g
	 * 400 Bad Request */
	protected abstract String getReason();
	/**
	 * Returns more headers for example redirect.
	 * or null if no additional headers required.*/
	protected abstract String getMoreHeaders();

	/**
	 * Writes the Status code to the output stream (client).*/
	public void write(OutputStream out) throws Exception {
        out.write(("HTTP/1.0 " + getReason() + CR).getBytes());
        out.write(("Content-Type: text/html" + CR).getBytes());
        out.write(("Connection: close" + CR).getBytes());
		if (getMoreHeaders() != null) {
			out.write((getMoreHeaders() + CR).getBytes());
		}
		out.write(CR.getBytes());
		out.write(
			("<html><body><hr><strong>" + getReason() + "</strong><hr><!--")
				.getBytes());

		Throwable t = this;
		while (t != null) {
			out.write(("\n" + t.toString()).getBytes());
			StackTraceElement[] elements = t.getStackTrace();
			if (elements != null) {
				for (int i = 0; i < elements.length; i++) {
					out.write(("\n" + elements[i].toString()).getBytes());
				}
			}
			t = t.getCause();
			out.write("\n".getBytes());
		}

		out.write("--></body></html>\n".getBytes());
	}
        
    
}

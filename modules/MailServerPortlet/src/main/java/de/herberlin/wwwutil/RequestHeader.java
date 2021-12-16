package de.herberlin.wwwutil;

import de.herberlin.wwwutil.httperror.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

/**
 * Parses a http request.
 * Should work for proxies and www servers.
 * 
 * Multipart formadata not supported!
 *
 * @author Hans Joachim Herbertz
 * @created 30.01.2003
 */
public class RequestHeader extends AbstractHeader {

	/**
	 * The method (GET, POST, HEAD etc)
	 * Methods are not validated but simply stored.
	 */
	protected String method = null;
	private URL url = null;
	private byte[] postData = null;

	/**
	 * Constructor for RequestHeader.
	 * @param in
	 * @throws Exception
	 */
	public RequestHeader(InputStream in) throws HttpError {
		headers = parseStream(in);
		// Handle post data
		if ("POST".equals(getMethod())) {

			if (getContentLength() != null) {

//				if (getHeader("Content-Type").indexOf("www-form-urlencoded")
//					!= -1) {

					ByteArrayOutputStream out = new ByteArrayOutputStream();
					int i = -1;
					InputStream cli =
						new ContentLengthInputStream(
							in,
							getContentLength().longValue());
					try {
						while ((i = cli.read()) != -1) {
							out.write(i);
						}
					} catch (IOException e) {
						throw new BadRequest_400(e);
					}
					postData = out.toByteArray();
//				} else {
//
//					// other than www-form-urlencoded
//					throw new NotImplemented_501();
//				}
			} else {

				// POST Request without content length
				throw new LengthRequired_411("POST request requires content-length header.");
			}

		}
		// Call get Host to notice header error
		getHost();
	}

	/**
	* @see de.herberlin.server.ui.AbstractHeader#parseFirstLine(String)
	*/
	protected void parseFirstLine(String firstLine) throws HttpError {
		this.firstLine = firstLine;
		StringTokenizer st = new StringTokenizer(firstLine, " ");
		if (st.countTokens() != 3) {
			throw new BadRequest_400("Invalid first header line: " + firstLine);
		}
		// method
		method = st.nextToken().trim().toUpperCase();
		// path and host
		String pathAndHost = st.nextToken().trim();
		// protocol
		protocol = st.nextToken().trim();
		url = makeUrl(pathAndHost);
	}

	public URL getUrl() {
		return url;
	}
	
	private URL makeUrl(String raw) throws BadRequest_400 {
		if (raw == null) {
			throw new BadRequest_400("Path may not be null.");
		}
		URL result = null;
		if (raw.startsWith("http")) {
			try {
				result = new URL(raw);
				host = result.getHost();
			} catch (MalformedURLException e) {
				throw new BadRequest_400("Bad path:" + raw + "," + e);
			}
		} else {
			try {
				result = new URL("http://localhost" + raw);
			} catch (MalformedURLException e) {
				throw new BadRequest_400("Bad path:" + raw + ", " + e);
			}
		}
		return result;
	}
	
	/**
	* @see de.herberlin.server.ui.AbstractHeader#writeFirstLine(OutputStream)
	*/
	protected void writeFirstLine(OutputStream out) throws HttpError {
		try {
			if (protocol.equalsIgnoreCase("http/1.1")) {
				out.write(
					(getMethod().toUpperCase()
						+ " "
						+ getUrl().toExternalForm()
						+ " HTTP/1.1"
						+ CR)
						.getBytes());
				// removeHeader("Host");
			} else if (protocol.equalsIgnoreCase("http/1.0")) {
				out.write(
					(getMethod().toUpperCase()
						+ " "
						+ getUrl().toExternalForm().substring(("http://"+getHost()).length())
						+ " HTTP/1.0"
						+ CR)
						.getBytes());
				setHeader("Host", getHost());
			} else {
				throw new HttpVersion_505(
					"Http Version not supportet: " + getProtocol());
			}
		} catch (IOException e) {
			throw new ConnectionLost(e);
		}
	}
	
	/**
	 * Returns the method.
	 * @return String
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * Gets the port; returns null if port not set.*/
	public Integer getPort() {
		if (url.getPort() == -1) {
			return null;
		} else {
			return new Integer(url.getPort());
		}
	}

	/**
	 * Path starts with "/".*/
	public String getPath() {
		return url.getPath();
	}

	public String toString() {
		return getMethod() + " " + getHost() + getPath();
	}

	/**
	 * Returns the post data if any.
	 * @return
	 */
	public byte[] getPostData() {
		return postData;
	}
	/**
	 * Gets the file type from the full path. File type is
	 * returned e.g. "html" or an empty String "" if no file
	 * type could be found.
	 * @return type eg. html or an empty string
	 */
	public String getFileType() {
		String path = url.getPath();
		int lastSlash = path.lastIndexOf('/');
		int lastDot = path.lastIndexOf('.');
		if (lastDot > lastSlash) {
			return path.substring(path.lastIndexOf('.') + 1);
		} else {
			return "";
		}
	}
}

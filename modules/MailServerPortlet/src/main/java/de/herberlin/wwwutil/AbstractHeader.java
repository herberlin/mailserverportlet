package de.herberlin.wwwutil;

import de.herberlin.wwwutil.httperror.BadRequest_400;
import de.herberlin.wwwutil.httperror.ConnectionLost;
import de.herberlin.wwwutil.httperror.HttpError;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** 
 * Base Class for parsing and writing http headers.
 *
 * @author Hans Joachim Herbertz
 * @created 29.01.2003
 */
public abstract class AbstractHeader implements Serializable {

	/**
	 * CRNL String.*/
	public static final String CR = "\r\n";
	/**
	 * True if the first line is expected to be the http 
	 * status code; set to false if first line is a simple header
	 * (as for cgi response);
	 */
	// protected boolean mustReadFirstLine = true;
	/**
	 * The first line for display purpose.
	 * If the firstLine is not null, the input streams
	 * first line is treated as a normal header.
	 */
	protected String firstLine = null;
	/** Holds headers. */
	protected Map headers = new HashMap();
	/**
	 * Protocol either http/1.0 or http/1.1.*/
	protected String protocol = "HTTP/1.1";
	/**
	 * The host name. */
	protected String host = null;

	/** Parses the first line of the header. */
	protected abstract void parseFirstLine(String firstLine) throws HttpError;
	/** Writes the first header line to the output stream. */
	protected abstract void writeFirstLine(OutputStream out) throws HttpError;

	/**
	 * Parses inputstream. Calls parseFirstLine(..) for
	 * first line parsing. 
	 * Please note: Some header Validations are made in
	 * write(out) and writeFirstLine(..).*/
	protected Map parseStream(InputStream in) throws HttpError {
		boolean foundCR = false; // keep NL for finding end of header
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int lineBreak = 10;
		int skipChar = 13;
		boolean crNotFound = true; // initializes cr char
		int b;
		while (true) {
			try {
				b = in.read();
			} catch (IOException e) {
				throw new ConnectionLost(e);
			}
			// REMOVE this
			if (b==-1)  throw new ConnectionLost("ClientIn read -1. Already read: "+headers);
			// Please Note:
			// Clients and servers should use chr(13)+chr(10) for 
			// linebreak in headers; anyway we are parsing for 
			// the first of them we find.
			if (crNotFound) {
				if (b == 10) {
					lineBreak = 10;
					skipChar = 13;
					crNotFound = false;
				} else if (b == 13) {
					lineBreak = 13;
					skipChar = 10;
					crNotFound = false;
				}
			}
			// end of line handling
			if (b == skipChar)
				continue; // New Line skipped
			buffer.write(b);
			if (b == lineBreak) {
				if (foundCR) {
					// if this is the second CR headers are terminated
					try {
						in.read();
					} catch (Throwable t) {
					}// read skipchar
					break;
				} else if (firstLine==null) {
					// The first line must be handled different for request and resporse
					parseFirstLine(new String(buffer.toByteArray()).trim());
					buffer = new ByteArrayOutputStream();

				} else {
					// end of line: parse buffer
					String t = new String(buffer.toByteArray());
					int pos = t.indexOf(':');
					if (pos > -1) {
						HeaderItem item =
							new HeaderItem(
								t.substring(0, pos).trim(),
								t.substring(pos + 1).trim());
						headers.put(item.getKey().toLowerCase(), item);
					} else {
						throw new BadRequest_400(
							"Error parsing header line: " + t);
					}
					buffer = new ByteArrayOutputStream();
					foundCR = true;
				} //~ if foundCR..
			} else {
				// not a CR read next
				foundCR = false;
			} //~ if b=lineBreak
		} //~ while true
		return headers;
	}
	/**
	 * Returns the value of the content length parameter
	 * or null if not set or not parseable. */
	public Integer getContentLength() {
		String cl=getHeader("Content-Length");
		if (cl!=null)	{
			try {
				return new Integer(cl);
			} catch (NumberFormatException t) {
				return null;
			}
		}else {
			return null;
		}
	}
	/**
	 * Returns the first line of the stream read.
	 * The first line e.g "GET /path/page HTTP/1.1"
	 * is not of key - value, therefor
	 * we cant store it as HeaderItem
	 * @return first line as String 
	 */
	public String getFirstLine() {
		return firstLine;
	}
	/**
	 * Writes HTTP header to an output stream. 
	 * Header is terminated by two CRLF*/
	public void write(OutputStream out) throws HttpError, IOException {
		writeFirstLine(out);
		Iterator it = headers.values().iterator();
		while (it.hasNext()) {
			HeaderItem item = (HeaderItem) it.next();
			if (item != null) {
				out.write(
					(item.getKey() + ": " + item.getValue() + CR).getBytes());
			}
		}
		// another CRLF for end of header
		out.write(CR.getBytes());
	}

	/**
	 * Gets an HeaderItem object containing the 
	 * key-value pair in original writing. 
	 * Keys are case insensitive.
	 * @return HeaderItem or null if not found. */
	public HeaderItem getHeaderItem(String name) {
		return (HeaderItem) headers.get(name.toLowerCase());
	}
	/**
	 * Gets the header value. Name-upper/lower case is ignored. 
	 * @return The value or null if header wasnt found.*/
	public String getHeader(String name) {
		HeaderItem item = getHeaderItem(name);
		if (item != null) {
			return item.getValue();
		} else {
			return null;
		}
	}
	/**
	 * Sets a header key-value pair. 
	 * Please note: Setting the Host-header will also update
	 * the host for url.*/
	public void setHeader(String key, String value) {
		HeaderItem item = new HeaderItem(key, value);
		headers.put(key.toLowerCase(), item);
		if (key.equalsIgnoreCase("host")) host=value;
	}
	
	/**
	 * Removes a header.
	 * @return HeaderItem containing the original header
	 * or null if header wasnt found. */
	public HeaderItem removeHeader(String key) {
		return (HeaderItem) headers.remove(key.toLowerCase());
	}

	/**
	 * Returns the protocol.
	 * @return String e.g. "HTTP/1.1"
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * Gets all headers as two dimensional String array
	 * getAllHeaders()[x][0] is the key, 
	 * getAllHeaders()[x][1] is the corresponding value. <br>
	 * This is an utility method used when giving the environment
	 * to CGIs.
	 */
	public String[][] getAllHeaders() {
		String[][] data=new String[headers.size()][2];
		Iterator it=headers.values().iterator();
		int counter=0;
		while (it.hasNext()) {
			HeaderItem item=(HeaderItem)it.next();
			data[counter][0]=item.getKey();
			data[counter][1]=item.getValue();
			counter++;
				
		}
		return data;
	}

	/**
	 * Gets the host.
	 * Host is taken from the first line or the Host - header.
	 * Returns null if host not found.
	 * This should be possible when running as www-server
	 * (not proxy server) with http/1.0. Please note that no exception
	 * is thrown in case of invalid HTTP-request (http/1.1 and no host given).
	 * The application using this class is responsible to do so.
	 */
	public String getHost() {
		if (host == null) {
			// host was not set with first line
			// try to find it in headers.

			host = getHeader("Host");
		}
		return host;
	}

	/**
	 * HeaderItem represents one line of the HTTP header, for
	 * example "Content-Type: text/html"<br>
	 * In this case "Content-Type" is the key and "text/html" the value.
	 * This is a very simple data class, all key and values are returned
	 * unchanged.
	 * */
	public class HeaderItem implements Serializable {
		private String key = null;
		private String value = null;

		/**
		 * Constructor creates a HeaderItem
		 */
		public HeaderItem(String key, String value) {
			this.key = key;
			this.value = value;
		}
		/**
		 * Gets the key as given in constructor
		 */
		public String getKey() {
			return key;
		}
		/**
		 * Gets the value as given in constructor
		 */
		public String getValue() {
			return value;
		}
		/**
		 * Returns a String representation of the object.
		 * @see Object#toString()
		 */
		public String toString() {
			return key + ": " + value;
		}
	}
	
}

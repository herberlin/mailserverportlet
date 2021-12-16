package de.herberlin.wwwutil;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Reads a give number "contentLength" of bytes from an InputStream, then
 * returns read(..) -1.
 * 
 * This class is used with http-streams when a content length is given.
 *
 * @author Hans Joachim Herbertz
 * @created 07.12.2002
 */
public class ContentLengthInputStream extends FilterInputStream {

	private final long contentLength;
	private long bytesRead = 0;

	/**
	 * Constructs a ContentLengthInputStream;
	 * 
	 * @param in
	 *            InputStream; should be BufferedInputStream
	 * @param contentLength
	 *            long: number of bytes to read.
	 */
	public ContentLengthInputStream(InputStream in, long contentLength) {
		super(in);
		if (contentLength < 0)
			throw new IllegalArgumentException("ContentLength: " + contentLength);
		this.contentLength = contentLength;
	}

	/**
	 * @see InputStream#read()
	 */
	public int read() throws IOException {
		int ret;
		if (bytesRead < contentLength) {
			ret = in.read();
			bytesRead++;
		} else {
			ret = -1;
		}
		return ret;
	}

	/**
	 * @see InputStream#read(byte[])
	 */
	public int read(byte[] b) throws IOException {
		for (int i = 0; i < b.length; i++) {
			int current = read();
			if (current == -1) {
				if (i == 0)
					return -1;
				else
					return i;
			} else {
				b[i] = (byte) current;
			}
		}
		return b.length;
	}

	/**
	 * This method is not implemented.
	 */
	public int read(byte[] b, int off, int len) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Method not allowed.");
	}

	/**
	 * @see InputStream#available()
	 */
	public int available() throws IOException {
		return Math.min(in.available(), (int) (contentLength - bytesRead));
	}

	/**
	 * @see InputStream#skip(long)
	 */
	public long skip(long n) throws IOException {
		long toSkip = Math.min(n, contentLength - bytesRead);
		long skipped = in.skip(toSkip);
		bytesRead += skipped;
		return skipped;
	}
}

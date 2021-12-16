package de.herberlin.wwwutil;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Writes the chunked - format to an output stream.
 *
 * @author Hans Joachim Herbertz
 * @created 01.02.2003
 */
public class ChunkedOutputStream extends FilterOutputStream {

	private final byte[] CR = new byte[]{13, 10};
	private int bufferSize = 1024;
	private int counter = 0;
	private byte[] buffer = null;
	/**
	 * Do not write CRNL at the beginning of 
	 * the stream, */
	private boolean isNew=true;
	/**
	 * Constructor for ChunkedOutputStream.
	 * @param out
	 */
	public ChunkedOutputStream(OutputStream out) {
		super(out);
	}

	public  void flush() throws IOException {
		if (buffer==null) return;
		if (!isNew) {
			out.write(CR);
		}
		isNew=false;
		out.write(Integer.toHexString(counter).getBytes());
		out.write(CR);
		out.write(buffer, 0, counter);
		out.flush();
		buffer = null;
	}

	public void close() throws IOException {
		flush(); // write current buffer
		// write terminating 0
		out.write(CR);
		out.write("0".getBytes());
		out.write(CR);
		out.write(CR);
		out.flush();
		super.close();
	}

	public void write(int b) throws IOException {
		if (buffer == null) {
			buffer = new byte[bufferSize];
			counter = 0;
		}
		buffer[counter]=(byte)b;
		counter++;
		if (counter>= bufferSize) {
			flush();
		}
	}
	/**
	 * Returns the bufferSize.
	 * @return int
	 */
	public int getBufferSize() {
		return bufferSize;
	}

	/**
	 * Sets the bufferSize.
	 * Stream will continue writing the current chunk
	 * until ists terminated, then switch to the new buffersize;
	 * 
	 * @param bufferSize The bufferSize to set
	 */
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

}

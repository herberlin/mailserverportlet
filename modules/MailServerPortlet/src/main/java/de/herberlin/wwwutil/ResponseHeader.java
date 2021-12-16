package de.herberlin.wwwutil;

import de.herberlin.wwwutil.httperror.BadGateway_502;
import de.herberlin.wwwutil.httperror.ConnectionLost;
import de.herberlin.wwwutil.httperror.HttpError;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Basic class handling http response headers. Use concrete
 * implementation as:
 *  - ProxyResponse (read everything from the input stream);
 *  - CgiResponse (read some cgi-headers but no first line from the cgi output)
 *  - SimpleResponse (read nothing from any stream); 
 *
 * @author Hans Joachim Herbertz
 * @created 01.02.2003
 */
public abstract class ResponseHeader extends AbstractHeader {

	/**
	 * the http status.*/
	protected Integer status = null;

	/**
	 * The http status message. */
	protected String statusMessage = null;


	/**
	 * @see AbstractHeader#parseFirstLine(String)
	 */
	protected void parseFirstLine(String firstLine) throws HttpError {
		this.firstLine=firstLine;
		String[] split=firstLine.split(" ",3);
		if (split.length<2) throw new BadGateway_502(firstLine);
		protocol=split[0].trim();
		try {
			status=new Integer(split[1]);
		} catch (NumberFormatException e) {
			throw new BadGateway_502(e);
		}
		if (split.length>2) {
			statusMessage=split[2].trim();
		}else {
			statusMessage="";
		}
	}
	/**
	 * Returns the http status conde (e.g. 404) */
	public Integer getStatus() {
		return status;
	}
	
	/**
	 * Returs the http status message. */
	public String getStatusMessage() {
		return statusMessage;
	}
	/**
	 * @see AbstractHeader#writeFirstLine(OutputStream)
	 */
	protected void writeFirstLine(OutputStream out) throws HttpError {
		try {
			out.write((getProtocol()+" "+getStatus()+ " "+getStatusMessage()+CR).getBytes());
		} catch (IOException e)  {
			throw new ConnectionLost(e);
		}
	}
	/**
	 * @see Object#toString()
	 */
	public String toString() {
		return getStatus()+" "+getStatusMessage();	
	}
}

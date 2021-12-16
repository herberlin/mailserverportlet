package de.herberlin.wwwutil;

import de.herberlin.wwwutil.httperror.HttpError;

import java.io.InputStream;

/**
 * Class representing the response headers of a cgi
 * response. That means parses the cgi - inputstream 
 * for any status code. 
 * 
 * @author hans joachim herbertz
 * created 28.12.2003
 */
public class CgiResponse extends ResponseHeader {

	public CgiResponse(InputStream in) throws HttpError {
		status=new Integer(200);
		statusMessage="OK";
		firstLine="HTTP/1.1 200 OK";
		headers = parseStream(in);
		if (getHeader("status")!=null) {
			status=new Integer(getHeader("status"));
			statusMessage="";
			firstLine="HTTP/1.1 "+status;
		} if (getHeader("location")!=null) {
            status=new Integer(302);
            statusMessage="Moved Temporarily";
            firstLine="HTTP/1.1 "+status+" "+statusMessage;
		}
	}
}

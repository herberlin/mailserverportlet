package de.herberlin.wwwutil;


/**
 * Simple response does not parse any stream;
 * headers are simply made by the setters.
 * 
 * @author hans joachim herbertz
 * created 28.12.2003
 */
public class SimpleResponse extends ResponseHeader {

	public SimpleResponse() {
		status=new Integer(200);
		statusMessage="OK";
		firstLine="HTTP/1.1 200 OK";
	}

}

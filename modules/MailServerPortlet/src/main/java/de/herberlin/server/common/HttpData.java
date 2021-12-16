/*
 * Created on 20.02.2004
 */
package de.herberlin.server.common;

import de.herberlin.server.common.event.FileData;
import de.herberlin.server.common.event.HttpRequestEvent;
import de.herberlin.wwwutil.RequestHeader;
import de.herberlin.wwwutil.ResponseHeader;

import java.io.File;
import java.io.OutputStream;
import java.net.InetAddress;


/**
 * Internal data structure
 */
public class HttpData  {
    
    
    /**
     * The requestHeaders as read from the request
     */
    public RequestHeader req=null;
    /**
     * The response headers collected by application
     */
    public ResponseHeader resp=null;
    /**
     * The file data for display purpose
     */
    public FileData fileData=null;
    /**
     * The requests mapped filename
     */
    public File realPath=null;
    /**
     * The clients output stream
     */
    public OutputStream out = null;
    /**
     * The requests mime type as given from configuration service
     */
    public String mimeType = null;
    /**
     * Request port
     */
    public int port = 80;
    /**
     * Request address (host)
     */
    public InetAddress inetAddress = null;
    private HttpRequestEvent event = null;

    public HttpData() {
        // create the event here to set the timestamp
        event = new HttpRequestEvent();
        fileData = event.getFileData();
    }

    public HttpRequestEvent asEvent() {
        event.setEndTimestamp(new Long(System.currentTimeMillis()));
        event.setRequest(req);
        event.setResponse(resp);
        return event;
    }
}
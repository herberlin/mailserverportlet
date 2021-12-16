package de.herberlin.server.common.event;

import de.herberlin.wwwutil.RequestHeader;
import de.herberlin.wwwutil.ResponseHeader;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Request event tells about treated requests.
 *
 * @author Hans Joachim Herbertz
 * @created 27.01.2003
 */
public class HttpRequestEvent extends RequestEvent implements Serializable {

	private Long endTimestamp = null;
	private RequestHeader request = null;
	private ResponseHeader response = null;
    private FileData fileData = null;
	private TimeData timeData = null;

	/**
	 * Constructor for LoggingEvent. */
	public HttpRequestEvent() {
		super();
	}

	/**
	 * Returns the endTimestamp.
	 * @return Long
	 */
	public Long getEndTimestamp() {
		return endTimestamp;
	}

	/**
	 * Sets the endTimestamp.
	 * @param endTimestamp The endTimestamp to set
	 */
	public void setEndTimestamp(Long endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

	/**
	 * Returns the request.
	 * @return RequestHeader
	 */
	public RequestHeader getRequest() {
		return request;
	}

	/**
	 * Sets the request.
	 * @param request The request to set
	 */
	public void setRequest(RequestHeader request) {
		this.request = request;
	}

	/**
	 * Returns the response.
	 * @return ResponseHeader
	 */
	public ResponseHeader getResponse() {
		return response;
	}

	/**
	 * Sets the response.
	 * @param response The response to set
	 */
	public void setResponse(ResponseHeader response) {
		this.response = response;
	}

	/**
	 * Sets the startTimestamp.
	 * @param startTimestamp The startTimestamp to set
	 */
	public void setStartTimestamp(Long startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	/**
	 * @see de.herberlin.server.ServerEvent.BasicEvent#getValueAt(int)
	 */
	public Object getValueAt(int tableColumn) {
		switch (tableColumn) {
			case 0 :
				return getTimeData();
			case 1 :
				return request;
			case 2 :
				return response;
			case 3 :
				return getFileData();
			default :
				return "";
		}
	}

	public String toString() {
		return getValueAt(0)
			+ " "
			+ getValueAt(1)
			+ " "
			+ getValueAt(2)
			+ " "
			+ getValueAt(3);
	}

	public FileData getFileData() {
        if (fileData==null) {
            fileData=new FileData();
        }
        return fileData;
	}

	private TimeData getTimeData() {
		if (timeData == null) {
			try {

				long responseTime=endTimestamp.longValue() - startTimestamp.longValue();
				long contentLength=getSendContentLength();
				double responseSpeed= ((double)contentLength)/((double)responseTime);
				DecimalFormat format=new DecimalFormat("#0.00");
				timeData = new TimeData();
				timeData.setFormattedTimestamp(getFormattedTimestamp());
				timeData.setUrl(request.getPath());
				String[][] displayValues = new String[5][2];
				timeData.setDisplayValues(displayValues);
				displayValues[0][0] = "Content-Type";
				displayValues[0][1] = response.getHeader("Content-type");
				displayValues[1][0] = "Start-Time";
				displayValues[1][1] = getFormattedTimestamp();
				displayValues[2][0] = "Response Time";
				displayValues[2][1] =format.format(((double)responseTime)/1000) + " s";
				displayValues[3][0] = "Content-Length";
				displayValues[3][1] = format.format(((double)contentLength)/1000) + " kb";
				displayValues[4][0] = "Speed";
				displayValues[4][1] = format.format(responseSpeed) + " kb/sec";
			} catch (Throwable t) {
				EventDispatcher.add(new ErrorEvent(t));
			}

		}
		return timeData;
	}

	private long getSendContentLength() {
		if (response.getContentLength()!= null) {
			return  response.getContentLength().longValue();
		} else if (fileData.getContentLength()>-1){
			return fileData.getContentLength();
		} else if (fileData.getBytes() !=null ){
				return (long) fileData.getBytes().length;
		} else {
			return -1;
		}
	}
}

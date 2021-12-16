package de.herberlin.server.common.event;


/**
 * Application Event tells about server and connection state.
 * @author Hans Joachim Herbertz
 * @created 12.02.2003
 */
public class ApplicationEvent extends ServerEvent {

	public static final short STATUS_NOT_SET=-1;
	public static final short SERVER_STARTED=1;
	public static final short SERVER_STOPPED=2;
	public static final short CONNECTION_ESTABLISHED=3;
	public static final short CONNECTION_CLOSED=4;

	private String message=null;
	private short status=STATUS_NOT_SET;
	/**
	 * Constructor for ApplicationEvent.
	 */
	public ApplicationEvent(short status) {
		super();
		this.status=status;
	}

	public ApplicationEvent(short status,String aMessage) {
		super();
		message=aMessage;
		this.status=status;
	}

	/**
	 * Returns the message.
	 * @return String
	 */
	public String getMessage() {
		if (message == null){
			switch (status) {
				case SERVER_STARTED:
					message = "Server started";
					break;
				case SERVER_STOPPED:
					message = "Server stopped";
					break;
			}
		}
		return message;
	}

	/**
	 * Sets the message.
	 * @param message The message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @see de.herberlin.server.ServerEvent.BasicEvent#getValueAt(int)
	 */
	public Object getValueAt(int tableColumn) {
		switch (tableColumn) {
			case 0:
				return getFormattedTimestamp();
			case 1:
				return getMessage();
			default:
				return "";
		}
	}

	public String toString() {
		return "Status="+status+" message="+message;
	}
	/**
	 * Returns the status.
	 * @return short
	 */
	public short getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 * @param status The status to set
	 */
	public void setStatus(short status) {
		this.status = status;
	}

}

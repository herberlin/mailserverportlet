package de.herberlin.server.common.event;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Hans Joachim Herbertz
 * @created 12.02.2003
 */
public abstract class ServerEvent implements Serializable {
	protected Long startTimestamp = null;
	private DateFormat dateFormat=new SimpleDateFormat("HH:mm:ss");
	/**
	 * Constructor for BasicEvent.
	 */
	public ServerEvent() {
		super();
		startTimestamp = new Long(System.currentTimeMillis());
	}

	/**
	 * Returns the startTimestamp.
	 * @return Long
	 */
	public Long getStartTimestamp() {
		return startTimestamp;
	}
	/**
	 * Returns the formatted timestamp of the object creation. */
	protected String getFormattedTimestamp() {
		return dateFormat.format(new Date(startTimestamp.longValue()));	
	}
	/**
	 * Creates displayable objects for log table display. */
	public abstract Object getValueAt(int tableColumn);

}

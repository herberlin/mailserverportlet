package de.herberlin.server.common.event;

/**
 * Event indicating that a certain server started
 * and diliveres server name and log table colum names
 * @author herberlin
 *
 */
public class ServerStartEvent extends ApplicationEvent {


	private Object[] columNames=null;

	/**
	 * Constructor for server start event
	 * @param serverName the display name of this server,e.g.
	 * "httpd" or proxy"
	 * @param columNames the event table log headlines for
	 * this server
	 */
	public ServerStartEvent(String serverName, Object[] columNames) {
		super(ApplicationEvent.SERVER_STARTED, serverName);
		this.columNames=columNames;
	}

	public Object getValueAt(int tableColumn) {
		if (columNames!=null && columNames.length>tableColumn) {
			return columNames[tableColumn];
		} else {
			return null;
		}
	}

	public Object[] getColumnNames() {
		return columNames;
	}
}

package de.herberlin.server.common.event;


/**
 *	Application event listener recieves application events.
 * @author Hans Joachim Herbertz
 * @created 16.02.2003
 */
public interface ServerEventListener {

	void onServerEvent(ServerEvent ev);
}

package de.herberlin.server.common.event;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 *
 * @author Hans Joachim Herbertz
 * @created 08.02.2003
 */
public class EventDispatcher {

	private static int noConnections = 0;


	public static void add(ServerEvent ev) {

		if (ev instanceof ApplicationEvent) {

			ApplicationEvent appEv = (ApplicationEvent) ev;

			switch (appEv.getStatus()) {
				case ApplicationEvent.SERVER_STOPPED :
				case ApplicationEvent.SERVER_STARTED :
					noConnections = 0;
					break;
				case ApplicationEvent.CONNECTION_ESTABLISHED :
					noConnections++;
					break;
				case ApplicationEvent.CONNECTION_CLOSED :
					noConnections = Math.min(0, noConnections--);
					break;
				default :
					break;
			}

		}
		fireEvent(ev);
	}

	/**
	 * Returns the number of connections open for the server.
	 */
	public static int getNoConnections() {
		// TODO: move connection count to logTableModel
		return noConnections;
	}
	
	// Event listener handling
	private static Set<ServerEventListener> listeners = Collections.synchronizedSet(new HashSet<ServerEventListener>());
	public static void addServerEventListener(ServerEventListener listener) {
		listeners.add(listener);
	}
	public static void removeServerEventListener(ServerEventListener listener) {
		listeners.remove(listener);
	}
	private static void fireEvent(ServerEvent ev) {
		Iterator<ServerEventListener> it = listeners.iterator();
		while (it.hasNext()) {
			ServerEventListener listener =
				(ServerEventListener) it.next();
			listener.onServerEvent(ev);
		}
	}
}

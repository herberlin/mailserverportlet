package de.herberlin.server.mail;

import java.net.Socket;

import de.herberlin.server.common.AbstractServer;
/**
 * Mail server waiting for clients
 * @author herberlin
 *
 */
public class MailServer extends AbstractServer {


	protected void process(Socket client) {
		new MailThread(client);

	}


}

package de.herberlin.server.mail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import de.herberlin.server.Logger;
import de.herberlin.server.common.event.ErrorEvent;
import de.herberlin.server.common.event.EventDispatcher;
import de.herberlin.server.common.event.MailRequestEvent;

/**
 * Mail worker thread
 * 
 * @author herberlin
 *
 */
public class MailThread implements Runnable {

	/**
	 * Some clients might send the QUIT command immedeately after content is
	 * terminated to avoid waiting for response. The server will wait for this
	 * command for this millies
	 */
	public static final long WAIT_FOR_NEXT_LINE = 2000;
	private Logger logger = Logger.getLogger(getClass().getName());
	private Socket socket = null;

	/**
	 * Logs the whole for display purpose
	 */
	private CharArrayWriter history = new CharArrayWriter();

	/**
	 * Constructor starts thread
	 * 
	 * @param socket
	 */
	public MailThread(Socket socket) {
		this.socket = socket;
		new Thread(this).start();
	}

	public void run() {

		BufferedReader in = null;
		BufferedWriter out = null;
		logger.debug("Mail thread started");

		// Event holds display infos
		MailRequestEvent event = new MailRequestEvent();

		try {

			String hostname = InetAddress.getLocalHost().getHostName();

			in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
			String aLine = null;

			// welcome message
			write(out, "220 " + hostname + " Simple Mail Transfer Service Ready");

			// content starts with DATA and ends with \n.\n
			boolean isContent = false;

			MailHeader mailHeader = new MailHeader();
			event.setFrom(mailHeader);
			while ((aLine = read(in)) != null) {
				if (isContent) {
					if (aLine.equals(".")) {
						isContent = false;
						// some clients might send next command
						// directly after the dot to avoid waiting
						// send response only if there is no next command
						Thread.sleep(WAIT_FOR_NEXT_LINE);
						if (!in.ready()) {
							write(out, "250 OK");
						}
					} else {
						// parse header
						mailHeader.addLine(aLine);
					}
				} else {

					// no content
					// reply somthing on commands
					String upper = aLine.toUpperCase().trim();

					if (upper.startsWith("HELO") || upper.startsWith("EHLO")) {
						// welcome message of the client
						write(out, "250 " + hostname);

					} else if (upper.startsWith("DATA")) {
						// content follows
						isContent = true;
						write(out, "354 Start mail input; end with <CRLF>.<CRLF>");

					} else if (upper.startsWith("RCPT")) {
						// check recipient
						write(out, "250 OK");
						event.addTo(aLine.substring("RCPT TO:".length()));

					} else if (upper.startsWith("RSET")) {
						// reset; do not send
						write(out, "250 OK");

					} else if (upper.startsWith("EXPN")) {
						// give mailinglist
						write(out, "250-Al Calico <ABC@MIT-MC.ARPA>");
						write(out, "250-<XYZ@MIT-AI.ARPA>");
						write(out, "250-Quincy Smith <SC-ISIF.ARPA:Q-Smith@ISI-VAXA.ARPA>");
						write(out, "250-<fred@BBN-UNIX.ARPA>");
						write(out, "250 <xyz@bar-unix.ARPA>");

					} else if (upper.startsWith("VRFY")) {
						// verify user
						write(out, "250 " + aLine.substring(Math.min(aLine.length(), 5)) + "@" + hostname);

					} else if (upper.startsWith("MAIL") || upper.startsWith("SEND") || upper.startsWith("SOML")
							|| upper.startsWith("SAML")) {
						// sender follows; send or mail things
						write(out, "250 OK");
						// event.setFrom(aLine.substring("MAIL
						// FROM:".length()));

					} else if (upper.startsWith("TURN")) {
						// deprecated; sender becomes recipient
						// and vice versa
						write(out, "502 REFUSE");

					} else if (upper.startsWith("NOOP")) {
						// do nothing
						write(out, "250 OK");

					} else if (upper.startsWith("HELP")) {
						// Ask for help
						write(out, "Can't help!");

					} else if (upper.startsWith("QUIT")) {
						// client terminates session
						write(out, "221 Closing transmission channel");
						break;
					} else {
						logger.warn("Not parsed: " + upper);
					}
				} // ~ no content

			}

		} catch (Exception e) {
			logger.error("run", e);
			EventDispatcher.add(new ErrorEvent(e));
		} finally {
			history.close();
			event.setConversation(history.toString());
			EventDispatcher.add(event);
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
				socket.close();
			} catch (IOException e) {
				logger.error("run", e);
				EventDispatcher.add(new ErrorEvent(e));
			}
		}
	}

	private String read(BufferedReader in) throws IOException {
		String l = in.readLine();
		logger.debug(l);
		if (l != null) {
			history.write(l);
			history.write("\n");
		}
		return l;
	}

	private void write(BufferedWriter out, String message) throws IOException {
		logger.debug(message);
		history.write(message);
		history.write("\n");
		out.write(message);
		out.newLine();
		out.flush();
	}

}

package de.herberlin.server.common;

import de.herberlin.server.Logger;
import de.herberlin.server.common.event.ApplicationEvent;
import de.herberlin.server.common.event.ErrorEvent;
import de.herberlin.server.common.event.EventDispatcher;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Abstract server implementation.
 * Subclasses may start different worker threads.
 * <p/>
 * Server fires events to de.herberlin.server.event.EventDispatcher,
 * add listeners there.
 *
 * @author Hans Joachim Herbertz
 * @created 27.01.2003
 */
public abstract class AbstractServer implements Runnable {

    protected Logger logger = Logger.getLogger(getClass().getName());
    /**
     * Servers state.
     */
    private boolean isRunning = false;
    /**
     * The port the server actually is running on
     */
    private int currentPort = -1;
    private Thread serverThread = null;

    /**
     * Returns true if the server is listening to
     * the given port, means Listener Thread is running.
     */
    public boolean isAlive() {
        return serverThread != null && serverThread.isAlive();
    }

    /**
     * Starts the server; if the server is already running does nothing.
     */
    public void start(int port) {
        if (isAlive())
            return;
        currentPort = port;
        isRunning = true;
        serverThread = new Thread(this);
        serverThread.start();
        Thread.yield();
        logger.debug("Server started: " + this);
        EventDispatcher.add(new ApplicationEvent(ApplicationEvent.SERVER_STARTED, getClass().getSimpleName() + " started."));
    }

    /**
     * Stops the server; if the server is not running does nothing.
     */
    public void stop() {
        if (!isAlive())
            return;
        isRunning = false;
        final String msg = getClass().getSimpleName() + " stopped.";
        new Thread() {
            public void run() {
                try {
                    String hostname = InetAddress.getLocalHost().getHostName();
                    Socket teardownClient = new Socket(hostname, currentPort);
                    OutputStream out = teardownClient.getOutputStream();
                    logger.debug("Shutting down: " + this);
                    out.write(("GET / http/1.0\nHost:blafasel\n\n\n").getBytes());
                    out.flush();

                    out.close();
                    teardownClient.close();
                    serverThread.join();
                    logger.debug("Server down: " + this);
                    EventDispatcher.add(new ApplicationEvent(ApplicationEvent.SERVER_STOPPED, msg));

                } catch (Exception t) {
                    logger.debug(t.getMessage(),t);
                }
            }
        }.start();
    }

    /**
     * @see Runnable#run()
     */
    public void run() {
        try {
            ServerSocket server = new ServerSocket(currentPort, 12);
            logger.debug("Waiting for client accept..");
            do {
                // accept
                Socket client = server.accept();
                client.setReuseAddress(true);
                client.setKeepAlive(true);
                if (isRunning) {
                    process(client);
                }

            } while (isRunning);
            server.close();
        } catch (Exception e) {
            logger.error("Error starting server", e);
            EventDispatcher.add(new ErrorEvent(e));
        } finally {
            isRunning = false;
        }

    }

    /**
     * @see Object#toString()
     */
    public String toString() {
        return getClass().getName()
                + " port="
                + getPort()
                + " isRunning="
                + isRunning;
    }

    /**
     * Subclass this for working implementations.
     *
     * @param client
     */
    protected abstract void process(Socket client);

    /**
     * Subclasses must return the port actually
     * configured
     *
     * @return
     */
    public int getPort() {
    	return currentPort;
    }


}

package de.herberlin.server.common.event;


/**
 * Error Event reports errors.
 *
 * @author Hans Joachim Herbertz
 * @created 16.02.2003
 */
public class ErrorEvent extends ServerEvent {

    private HttpRequestEvent loggingEvent = null;
    private Throwable error = null;
    private String message = null;

    public ErrorEvent(String message) {
        this.message = message;
    }

    public ErrorEvent(Throwable t) {
        this.error = t;
    }

    public ErrorEvent(Throwable t, HttpRequestEvent e) {
        this.error = t;
        this.loggingEvent = e;
    }

    public Object getValueAt(int pos) {
        if (loggingEvent != null) {
            return loggingEvent.getValueAt(pos);
        } else if (error != null) {
            return error.toString();
        } else {
            return getClass().getSimpleName();
        }
    }

    /**
     * Returns the error.
     *
     * @return Throwable
     */
    public Throwable getError() {
        return error;
    }

    /**
     * Sets the error.
     *
     * @param error The error to set
     */
    public void setError(Throwable error) {
        this.error = error;
    }

    /**
     * Returns the loggingEvent.
     *
     * @return LoggingEvent
     */
    public HttpRequestEvent getLoggingEvent() {
        return loggingEvent;
    }

    /**
     * Sets the loggingEvent.
     *
     * @param loggingEvent The loggingEvent to set
     */
    public void setLoggingEvent(HttpRequestEvent loggingEvent) {
        this.loggingEvent = loggingEvent;
    }

    public String toString() {
        if (error != null) {
            return error.toString();
        } else {
            return null;
        }
    }

    /**
     * Returns the message.
     *
     * @return String
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message.
     *
     * @param message The message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

}

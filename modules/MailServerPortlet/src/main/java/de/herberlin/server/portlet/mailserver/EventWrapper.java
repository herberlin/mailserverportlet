package de.herberlin.server.portlet.mailserver;

import de.herberlin.server.common.event.MailRequestEvent;
import de.herberlin.server.mail.MailHeader;
import de.herberlin.server.mail.MailUtil;

import java.io.Serializable;

public class EventWrapper implements Serializable {

    private static final long serialVersionUID = 1L;
    private MailRequestEvent event = null;

    public EventWrapper(MailRequestEvent event) {
        this.event = event;
    }

    public String getTo() {
        return MailUtil.decodeQuotedPrintableLine(event.getTo());
    }

    public String getDate() {
        return (String) event.getValueAt(0);
    }

    public MailHeader getMailHeader() {
        return (MailHeader) event.getValueAt(1);
    }

    public String toString() {
        return getClass() + " " + event.toString();
    }

    public String getFrom() {
        return MailUtil.decodeQuotedPrintableLine((String) getMailHeader().getHeaders().get("FROM"));
    }

    public String getSubject() {
        return MailUtil.decodeQuotedPrintableLine((String) getMailHeader().getHeaders().get("SUBJECT"));
    }
}

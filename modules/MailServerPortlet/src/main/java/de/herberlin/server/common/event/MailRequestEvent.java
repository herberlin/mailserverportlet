package de.herberlin.server.common.event;

import de.herberlin.server.mail.MailHeader;

public class MailRequestEvent extends RequestEvent {

	private String conversation = null;
	private StringBuilder to = new StringBuilder();
	private MailHeader mailHeader = null;

	public MailRequestEvent() {
		super();
	}

	public void setConversation(String history) {
		this.conversation = history;
	}

	public Object getValueAt(int tableColumn) {
		switch (tableColumn) {
		case 0:
			return getFormattedTimestamp();
		case 1:
			return mailHeader;
		case 2:
			return to;
		case 3:
			return getHeaders().getHeaders().getProperty("CONTENT-TYPE");
		default:
			return "";
		}
	}

	public void setFrom(MailHeader from) {
		this.mailHeader = from;
	}

	public void addTo(String to) {
		if (!(this.to.length() == 0)) {
			this.to.append(", ");
		}
		this.to.append(to);
	}

	public String getTo() {
		return to.toString();
	}
	public String getConversation() {
		return conversation;
	}
	
	public MailHeader getHeaders() {
		return mailHeader; 
	}

}

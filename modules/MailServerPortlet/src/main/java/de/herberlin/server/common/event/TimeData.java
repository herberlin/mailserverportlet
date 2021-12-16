package de.herberlin.server.common.event;


import java.io.Serializable;

/**
 *
 * @author Hans Joachim Herbertz
 * @created 13.02.2003
 */
public class TimeData implements Serializable {

	private String[][] displayValues = new String[0][0];
	private String formattedTimestamp = null;
	private String url = null;
	
	public TimeData() {
		super();
	}
	public String[][] getDisplayValues() {
		return displayValues;
	}

	/**
	 * Sets the displayValues.
	 *
	 * @param displayValues The displayValues to set
	 */
	public void setDisplayValues(String[][] displayValues) {
		this.displayValues = displayValues;
	}

	public String getURL() {
		return url;
	}

	public String toString() {
		return formattedTimestamp;
	}

	public void setFormattedTimestamp(String timestamp) {
		this.formattedTimestamp = timestamp;
	}

	/**
	 * Returns the url.
	 * @return String
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the url.
	 * @param url The url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

}

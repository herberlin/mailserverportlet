package de.herberlin.server.httpd;

import java.util.prefs.Preferences;

public interface HttpServerConfig {

	Preferences getMimeTypes();
	Preferences getExtensions();
	Preferences getAliasDirectories();
	String getDefaultPages();
	// System.getProperty("user.dir")
	String getDocRoot();
	String getHost();
	String getVersion();
	int getDelay();
	boolean isNoCaching();
	boolean useDefaultPages();
	String getJsonEventPath();
	
	public static class Factory{
		
		private static HttpServerConfig config = null; 
		
		public static HttpServerConfig getConfig() {
			return config; 
		}
		public static void setConfig(HttpServerConfig config){
			Factory.config = config; 
		}
	}
	
	
}

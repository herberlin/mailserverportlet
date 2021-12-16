package de.herberlin.server;

import java.util.Date;

/**
 * Created by aherbertz on 01.08.17.
 */

public abstract class Logger {

	public static interface LogFactory {
		Logger getLogger(String name);
	}

	private static LogFactory logFactory = null;

	public static void setLogFactory(LogFactory factory) {
		logFactory = factory;
	}

	public static Logger getLogger(String name) {
		Logger result = null;
		try {
			if (logFactory == null) {
				result = new FallbackLogger(name);
			} else {
				result = logFactory.getLogger(name);
			}
		} catch (Exception e) {
			System.err.println("Could not instanciate logger: " + name);
			e.printStackTrace();
			result = new FallbackLogger(name);
		}
		return result;
	}

	protected String name = null; 
	public void setName(String name) {
		this.name = name;
	}

	public abstract void debug(String s);

	public abstract void debug(String s, Exception e);

	public abstract void info(String s);

	public abstract void info(String s, Exception e);

	public abstract void warn(String s);

	public abstract void warn(String s, Exception e);

	public abstract void error(String s, Exception e);

	public abstract void error(String s);

	private static class FallbackLogger extends Logger {


		private FallbackLogger(String name) {
			this.name = name;
		}

		@Override
		public void setName(String name) {
			this.name = name;
		}

		@Override
		public void debug(String s) {
			print("debug",s, null);
		}

		@Override
		public void debug(String s, Exception e) {
			print("debug",s, e);
		}


		@Override
		public void info(String s) {
			print("error",s, null);
		}

		@Override
		public void info(String s, Exception e) {
			print("info",s, e);
		}

		@Override
		public void warn(String s) {
			print("warn",s, null);
		}

		@Override
		public void warn(String s, Exception e) {
			print("warn",s, e);
		}

		@Override
		public void error(String s, Exception e) {
			print("error",s, e);
		}

		@Override
		public void error(String s) {
			print("error", s, null);
		}

		private void print(String p, String s, Exception e) {
			Date t = new Date();
			System.err.printf("%tH%tM%tS %s %s: %s%n",t,t,t,p.toUpperCase(),name, s);
			if (e != null) {
				e.printStackTrace();
			}
		}
	}
}

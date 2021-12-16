package de.herberlin.server.portlet.mailserver.logging;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import de.herberlin.server.Logger;
import de.herberlin.server.Logger.LogFactory;

public class LoggingFactory implements LogFactory {

	@Override
	public Logger getLogger(String name) {
		return new LoggerImpl(name);
	}

	public static class LoggerImpl extends Logger {

		private Log log = null;

		private LoggerImpl(String name) {
			this.name = name;
			this.log = LogFactoryUtil.getLog(name);

		}

		@Override
		public void debug(String s) {
			log.debug(s);
		}

		@Override
		public void debug(String s, Exception e) {
			log.debug(s, e);
		}

		@Override
		public void info(String s) {
			log.info(s);
		}

		@Override
		public void info(String s, Exception e) {
			log.info(s, e);
		}

		@Override
		public void warn(String s) {
			log.warn(s);
		}

		@Override
		public void warn(String s, Exception e) {
			log.warn(s, e);
		}

		@Override
		public void error(String s, Exception e) {
			log.error(s, e);
		}

		@Override
		public void error(String s) {
			log.error(s);
		}

	}

}

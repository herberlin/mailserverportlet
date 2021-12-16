package de.herberlin.server.portlet.mailserver;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

/**
 * General purpose runtime exception.
 * @author aherbertz
 *
 */
public class AppException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AppException() {
    }

    public AppException(String message) {
        super(message);
    }

    public AppException(Throwable cause) {
        super(cause);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppException log(Log logger) {
        Log log = logger;
        if (log == null) {
            log = LogFactoryUtil.getLog(getClass());
        }
        Throwable cause = getCause();
        if (cause == null) {
            cause = this;
        }
        logger.error(getMessage(), cause);
        return this;
    }
}

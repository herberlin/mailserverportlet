package de.herberlin.server.portlet;

import com.liferay.mail.kernel.service.MailServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.PortalPreferencesLocalServiceUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.Validator;
import de.herberlin.server.Logger;
import de.herberlin.server.common.event.EventDispatcher;
import de.herberlin.server.common.event.MailRequestEvent;
import de.herberlin.server.common.event.ServerEvent;
import de.herberlin.server.common.event.ServerEventListener;
import de.herberlin.server.mail.MailServer;
import de.herberlin.server.portlet.constants.MailServerPortletKeys;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import javax.portlet.*;

import de.herberlin.server.portlet.logging.LoggingFactory;
import de.herberlin.server.portlet.mailserver.AppException;
import de.herberlin.server.portlet.mailserver.EventWrapper;
import de.herberlin.server.portlet.mailserver.Testmail;
import org.osgi.service.component.annotations.Component;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author aherbertz
 */
@Component(
        immediate = true,
        property = {
                "com.liferay.portlet.display-category=category.sample",
                "com.liferay.portlet.header-portlet-css=/css/main.css",
                "com.liferay.portlet.css-class-wrapper=mail-server-portlet",
                "com.liferay.portlet.instanceable=false",
                "javax.portlet.display-name=MailServer",
                "javax.portlet.init-param.template-path=/",
                "javax.portlet.init-param.view-template=/mailserver.jsp",
                "javax.portlet.name=" + MailServerPortletKeys.MAILSERVER,
                "javax.portlet.resource-bundle=content.Language",
                "javax.portlet.security-role-ref=power-user,user"
        },
        service = Portlet.class
)
public class MailServerPortlet extends MVCPortlet implements ServerEventListener {


    private static MailServer _server = null;
    private static List<EventWrapper> c_eventList = new LinkedList<EventWrapper>();

    protected Log getLog() {
        return LogFactoryUtil.getLog(getClass());
    }

    @Override
    public void destroy() {
        stopServer();
        super.destroy();
    }

    public void stopServer(ActionRequest request, ActionResponse response) {
        stopServer();
    }

    public void startServer(ActionRequest request, ActionResponse response) {
        try {
            startServer();
        } catch (SystemException e) {
            getLog().error(e, e);
            throw new AppException(e);
        }
    }

    public void sendTestmail(ActionRequest request, ActionResponse response) {

        Testmail.sendTestmail();

    }

    private void configureLiferay(String autostart, String host, String port) throws ReadOnlyException, ValidatorException, IOException, SystemException {

        // as of com.liferay.portlet.admin.action.EditServerAction
        PortletPreferences prefs = getMailPrefs();
        prefs.setValue(PropsKeys.MAIL_SESSION_MAIL_SMTP_HOST, host);
        prefs.setValue(PropsKeys.MAIL_SESSION_MAIL_SMTP_PORT, port);
        prefs.setValue(SETTING_AUTOSTART, autostart);
        prefs.store();
        MailServiceUtil.clearSession();


    }

    private PortletPreferences getMailPrefs() throws SystemException {
        PortletPreferences portletPreferences = PortalPreferencesLocalServiceUtil
                .getPreferences(PortletKeys.PREFS_OWNER_ID_DEFAULT, PortletKeys.PREFS_OWNER_TYPE_COMPANY);
        return portletPreferences;
    }

    public void configure(ActionRequest request, ActionResponse response) {

        try {

            String host = ParamUtil.get(request, "host", "");
            String port = ParamUtil.get(request, "port", "");
            String autostart = ParamUtil.get(request, "autostart", "false");

            if (Validator.isNotNull(host) && Validator.isNotNull(port)) {


                stopServer();
                configureLiferay(autostart, host, port);

                new Thread() {

                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            startServer();
                        } catch (Exception e) {
                            getLog().error(e, e);
                        }
                    }

                }.start();

            }
        } catch (Exception e) {
            getLog().error(e, e);
            SessionErrors.add(request, e.getClass());
        }

    }

    public static final String SETTING_AUTOSTART = de.herberlin.server.portlet.MailServerPortlet.class.getName() + ".autostart";

    @Override
    public void doView(RenderRequest request, RenderResponse response) throws IOException, PortletException {

        request.setAttribute("eventList", c_eventList);

        request.setAttribute("serverRunning", _server != null && _server.isAlive());
        request.setAttribute("serverPort", _server != null ? _server.getPort() : null);
        request.setAttribute("configTab", ParamUtil.get(request, "configTab", "config"));

        try {
            PortletPreferences prefs = getMailPrefs();
            request.setAttribute("autostart", prefs.getValue(SETTING_AUTOSTART, "false"));
            request.setAttribute("host", prefs.getValue(PropsKeys.MAIL_SESSION_MAIL_SMTP_HOST, ""));
            request.setAttribute("port", prefs.getValue(PropsKeys.MAIL_SESSION_MAIL_SMTP_PORT, ""));
        } catch (SystemException e) {
            getLog().error(e, e);
        }

        super.doView(request, response);
    }

    @Override
    public void init() throws PortletException {

        EventDispatcher.addServerEventListener(this);
        Logger.setLogFactory(new LoggingFactory());

        try {
            if ("true".equals(getMailPrefs().getValue(SETTING_AUTOSTART, "false"))) {
                startServer();
            }
        } catch (Exception e) {
            getLog().error(e, e);
        }
        super.init();
    }

    private void startServer() throws SystemException {
        if (_server == null) {
            _server = new MailServer();
        }
        if (!_server.isAlive()) {
            getLog().debug("Starting Mailserver.");
            String port = getMailPrefs().getValue(PropsKeys.MAIL_SESSION_MAIL_SMTP_PORT, "2525");
            _server.start(Integer.valueOf(port));
        } else {
            getLog().debug("Server already running.");
        }
    }

    private void stopServer() {
        if (_server != null && _server.isAlive()) {
            getLog().debug("Stopping Mailserver");
            _server.stop();
        } else {
            getLog().debug("Wont stop, server not running.");
        }
    }

    public static final int EVENT_LIST_LENGTH = 30;

    @Override
    public void onServerEvent(ServerEvent ev) {
        if (ev instanceof MailRequestEvent) {
            c_eventList.add(0, new EventWrapper((MailRequestEvent) ev));
            if (c_eventList.size() > EVENT_LIST_LENGTH) {
                c_eventList.remove(EVENT_LIST_LENGTH);
            }
        }
    }

}
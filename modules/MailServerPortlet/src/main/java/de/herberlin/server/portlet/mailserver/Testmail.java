package de.herberlin.server.portlet.mailserver;

import com.liferay.mail.kernel.model.MailMessage;
import com.liferay.mail.kernel.service.MailServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;

public class Testmail {

	private static Log log = LogFactoryUtil.getLog(Testmail.class);
	
	public static void sendTestmail() {
		try {
			boolean html =true; 
//			MailEngine.send(make("from"), make("to", 2), make("cc", 3), make("bcc", 2), "Subject",
//					"<h1>Body</h1>\n<p>Lorem ipsum</p><ul><li>eins</li>\n<li>zwei</li></ul>", html, make("replyTo", 3), "messageId123", "inReplyTo456");

			MailMessage mailMessage = new MailMessage(make("from"), make("to"), "Subject",
					"<h1>Body</h1>\n<p>Lorem ipsum</p><ul><li>eins</li>\n<li>zwei</li></ul>", html);
			mailMessage.setCC(make("cc", 3));
			mailMessage.setBCC(make("bcc", 2));
			mailMessage.setReplyTo(make("replyTo", 3));
			MailServiceUtil.sendEmail(mailMessage);
		} catch (Exception e) {
			log.error(e, e);
		}
	}
	
	private static InternetAddress make(String name) throws UnsupportedEncodingException {
		return new InternetAddress(name + "@example.com", name);
	}

	private static InternetAddress[] make(String name, int count) throws UnsupportedEncodingException {
		InternetAddress[] result = new InternetAddress[count];
		for (int i = 0; i < count; i++) {
			result[i] = make(name + i);
		}
		return result;
	}
}

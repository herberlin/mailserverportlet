package de.herberlin.server.mail;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class MailUtilTest {

    // https://www.utf8-zeichentabelle.de/unicode-utf8-table.pl
    @Test
    public void decodeQuotedPrintableLine() {
        String s = "E-Mailben=61chrichtigungen in der Jobb=C3=B6rse.=";
        String result = MailUtil.decodeQuotedPrintableLine(s);
        Assert.assertEquals("E-Mailbenachrichtigungen in der Jobbörse.", result);
    }

    @Test
    public void decodeQuotedPrintableLine1() {
        String s = "E-Mailben=61chrichtigungen in der Jobb=C3=B6rse.";
        String result = MailUtil.decodeQuotedPrintableLine(s);
        Assert.assertEquals("E-Mailbenachrichtigungen in der Jobbörse.\n", result);
    }

    @Test
    public void decodeQuotedPrintableSubject() {
        String s = "=?UTF-8?Q?E-Mailbenachrichtigungen in der Gr=C3=BCnen Jobb=C3=B6rse?=";
        String result = MailUtil.decodeQuotedPrintableLine(s);
        Assert.assertEquals("E-Mailbenachrichtigungen in der Grünen Jobbörse", result);
    }

    @Test
    public void decodeQuotedPrintableAddress() {
        String s = "=?UTF-8?Q?Admin Gr=C3=BCn?= <admin@liferay.com>";
        String result = MailUtil.decodeQuotedPrintableLine(s);
        Assert.assertEquals("Admin Grün <admin@liferay.com>", result);
    }
    @Test
    public void decodeEquals() {
        String s = "editJobNotifications=3Dtrue&confirmEmail=3Da130212c4ce1";
        String result = MailUtil.decodeQuotedPrintableLine(s);
        Assert.assertEquals("editJobNotifications=true&confirmEmail=a130212c4ce1", result);
    }


}
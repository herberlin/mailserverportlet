package de.herberlin.server.mail;

import de.herberlin.server.Logger;

import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MailUtil
 *
 * @author Hans Joachim Herbertz
 * @created 2020-04-11
 */
public class MailUtil {

    private static final Pattern pattern = Pattern.compile(".*(=\\w{2}).*", Pattern.DOTALL);

    /**
     * TODO: Fix this;
     * Two diget characters are not recognized.
     * Replacement of equal - sign (=) fails, tries to replace it once more
     */
    public static String decodeQuotedPrintableLine(String aLine) {
        if (aLine == null) {
            return aLine;
        }
        if (aLine.endsWith("=")) {
            aLine = aLine.substring(0, aLine.length() - 1);
        } else {
            aLine += "\n"; // Shorter lines need a real linebreak;
        }
        boolean match = false;
        try {
            do {
                Matcher matcher = pattern.matcher(aLine);
                match = matcher.matches() && matcher.groupCount() > 0;
                if (match) {
                    String m = matcher.group(1);
                    String mm = m.replaceAll("=C3", "");
                    mm = mm.replaceAll("=", "");
                    int val = Integer.valueOf(mm, 16);
                    String replacement = String.valueOf((char) val);
                    aLine = aLine.replaceFirst(m, replacement);
                }

            } while (match);
        } catch (Exception e) {
            Logger.getLogger(MailUtil.class.getName()).error("Error decoding " + aLine, e);
        }
        return aLine;
    }

    public static String decodeBase64Line(String aLine) {
        byte[] result = Base64.getMimeDecoder().decode(aLine);
        return new String(result);
    }

    // Da muss vermutlich ein eigener algorithmus etwickelt werden
    // https://www.utf8-zeichentabelle.de/unicode-utf8-table.pl
    public static void main(String[] args) {
        String s = "du hast gerade die E-Mailben=B6chrichtigungen in der Jobb=C3=B6rse abonniert.";
        Integer.valueOf("c3b6");
        String result = MailUtil.decodeQuotedPrintableLine(s);
    }
}

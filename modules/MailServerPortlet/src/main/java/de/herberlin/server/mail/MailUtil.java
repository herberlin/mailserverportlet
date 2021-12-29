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

    private static final Pattern pattern_long = Pattern.compile(".*(=[Cc][2-9]=[1-9a-fA-F]{2}).*", Pattern.DOTALL);
    private static final Pattern pattern_short = Pattern.compile(".*(=[1-9a-fA-F]{2}).*", Pattern.DOTALL);


    public static String decodeQuotedPrintableLine(String aLine) {
        if (aLine == null) {
            return aLine;
        }
        boolean isSubject = aLine.contains("=?UTF-8?Q?");
        if (isSubject) {
            aLine = aLine.replaceAll("=\\?UTF-8\\?Q\\?", ""); // utf-8 header in subject and addresses
            aLine = aLine.replaceAll("\\?=", "");
            aLine = aLine.replaceAll("_", " ");

        }

        if (aLine.endsWith("=")) {
            aLine = aLine.substring(0, aLine.length() - 1);
        } else if (!isSubject) {
            aLine += "\n"; // Shorter lines need a real linebreak;
        }

        boolean match = false;
        try {
            do {
                Matcher matcher = pattern_long.matcher(aLine);
                match = matcher.matches() && matcher.groupCount() > 0;
                if (match) {
                    String m = matcher.group(1);
                    int val = Integer.valueOf(m.substring(4), 16);
                    int factor = Integer.valueOf(m.substring(2,3))-2;
                    String replacement = String.valueOf((char) (val+(64*factor)));
                    aLine = aLine.replaceFirst(m, replacement);

                } else {
                    matcher = pattern_short.matcher(aLine);
                    match = matcher.matches() && matcher.groupCount() > 0;
                    if (match) {
                        String m = matcher.group(1);
                        int val = Integer.valueOf(m.substring(1), 16);
                        String replacement = String.valueOf((char) val);
                        aLine = aLine.replaceFirst(m, replacement);
                    }

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


}

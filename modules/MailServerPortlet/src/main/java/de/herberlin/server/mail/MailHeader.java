package de.herberlin.server.mail;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.UUID;

import de.herberlin.server.Logger;
import de.herberlin.server.httpd.HttpServerConfig;

/**
 * TODO quoted printable encoding TODO read multipart message
 */
public class MailHeader {

	private Properties headers = new Properties();
	private StringBuffer content = new StringBuffer();
	boolean readingHeaders = true;

	public MailHeader() {
	}

	public String getContent() {
		return content.toString().trim();
	}

	public String[][] getHeader() {
		String[][] result = new String[headers.size()][2];
		Iterator it = headers.keySet().iterator();
		int counter = 0;
		while (it.hasNext()) {
			String key = (String) it.next();
			String value = (String) headers.get(key);
			result[counter][0] = key;
			result[counter][1] = value;
			counter++;
		}
		return result;
	}

	private String boundary = null;
	private MailHeader mailPart = null;

	public void addLine(String aLine) {
		if (aLine == null)
			return;
		if (readingHeaders) {
			if ("".equals(aLine)) {
				// end of headers
				readingHeaders = false;
				return;
			}
			String[] parsed = aLine.split(":", 2);
			if (parsed.length == 1 && parsed[0] != null && parsed[0].indexOf("boundary") > -1) {
				String[] parts = aLine.split("=", 2);
				boundary = parts[1];
				if (boundary.startsWith("\"") && boundary.endsWith("\"")) {
					boundary = boundary.substring(1, boundary.length() - 1);
				}
				boundary = "--" + boundary;
				headers.put("CONTENT-TYPE", headers.get("CONTENT-TYPE") + parsed[0]);
			}
			if (parsed.length < 2 || parsed[0] == null || parsed[1] == null)
				return;
			headers.put(parsed[0].trim().toUpperCase(), parsed[1].trim());
		} else if (boundary != null && aLine.startsWith(boundary)) {
			// start of a new part
			if (mailPart != null) {
				content.append("\n<div>" + mailPart.fulFill() + "</div>\n");
			}
			mailPart = new MailHeader();
		} else if (mailPart != null) {
			mailPart.addLine(aLine);

		} else {
			// non partial content
			String contentEncoding = getHeaders().getProperty("CONTENT-TRANSFER-ENCODING");
			if ("quoted-printable".equalsIgnoreCase(contentEncoding)) {
				aLine = MailUtil.decodeQuotedPrintableLine(aLine);
			}else if ("base64".equalsIgnoreCase(contentEncoding)) {
				aLine = MailUtil.decodeBase64Line(aLine);
			} else {
				if (content.length() > 0) {
					content.append("\n");
				}
			}
			content.append(aLine);
		}
	}

	private String fulFill() {
		String contentDisposition = getHeaders().getProperty("CONTENT-DISPOSITION");
		if (contentDisposition != null) {
			// Content-Disposition: attachment; filename=about-mail.txt
			String match = "filename=";
			int start = contentDisposition.indexOf(match) + match.length();
			String filename;
			if (start >= 0) {
				filename = contentDisposition.substring(start);
			} else {
				filename = UUID.randomUUID().toString();
			}

			String path = saveContent(filename);

			return "Attachment: <a href=\"" + path + "\">" + filename + "</a>";

		} else {
			return content.toString();
		}
	}

	private String saveContent(String filename) {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss-");
		HttpServerConfig config = HttpServerConfig.Factory.getConfig();
		String docroot = null;
		if (config != null) {
			docroot = config.getDocRoot();
		} else {
			docroot = System.getProperty("user.dir");
		}
		Path path = Paths.get(docroot, "attachments", format.format(new Date()) + filename);
		try {
			Files.createDirectories(Paths.get(docroot, "attachments"));
			path = Files.createFile(path);
			path = Files.write(path, content.toString().getBytes(),  StandardOpenOption.WRITE);
			return path.toString();
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).error("Error creating file " + filename, e);
		}
		return null;
	}

	public Properties getHeaders() {
		return headers;
	}

	public String toString() {
		return (String) headers.get("FROM");
	}
}

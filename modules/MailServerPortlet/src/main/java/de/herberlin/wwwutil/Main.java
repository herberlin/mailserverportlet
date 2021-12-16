package de.herberlin.wwwutil;

import java.io.IOException;
import java.util.Properties;

/**
 * Prints version information.
 *
 * @author hans joachim herbertz created 08.01.2004
 */
public class Main {


	public static void main(String[] args) {
		Properties props = new Properties();
		try {
			props.load(
					Main.class.getResourceAsStream("/META-INF/maven/de.herberlin.bremsserver/wwwutil/pom.properties"));
			System.out.println(props.get("groupId"));
			System.out.println(props.get("artifactId"));
			System.out.println(props.get("version"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

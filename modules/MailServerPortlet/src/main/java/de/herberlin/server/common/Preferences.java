package de.herberlin.server.common;

/**
 * Facade for java.util.prefs.Preferences
 * Created by aherbertz on 11.05.16.
 */
public interface Preferences {
    void store();

    boolean nodeExists(String name);

    Preferences node(String name);

    void put(String jpeg, String s);

    int getInt(String s, int i);

    void putInt(String s, int port);

    String get(String httpDocroot, String property);

    boolean getBoolean(String httpNoCachingHeaders, boolean b);

    String[] keys();

    void putBoolean(String key, boolean isChecked);
}

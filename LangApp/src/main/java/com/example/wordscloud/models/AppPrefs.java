package com.example.wordscloud.models;

import java.util.HashMap;

public class AppPrefs implements IDBPrefs {
    public static AppPrefs getEmpty() {
        return new AppPrefs();
    }

    private DBType dbType;
    private HashMap<String, String> m = new HashMap<>();

    public DBType dbType() {
        return dbType;
    }

    @Override
    public void put(String key, String value) {
        if (key.equals("dbtype"))
            dbType = DBType.values()[Integer.parseInt(value)];
        else
            m.put(key, value);
    }

    @Override
    public String get(String key) {
        if (!key.equals("dbtype") && m.containsKey(key))
            return m.get(key);
        return "";
    }
}

package com.example.wordscloud.models;

public interface IDBPrefs {
    DBType dbType();
    void put(String key, String value);
    String get(String key);
}

package com.example.wordscloud.db;

import com.example.wordscloud.models.AppPrefs;
import com.example.wordscloud.models.IDBPrefs;

public class DBCreator {
    static private DBCreator instance;

    private IDBPrefs appPrefs;
    private IDB currentDB;

    static public DBCreator getInstance() {
        if (instance == null)
            instance = new DBCreator();
        return instance;
    }

    public void setAppPrefs(IDBPrefs appPrefs) {
        this.appPrefs = appPrefs;
    }

    public IDB GetDBInstance() {
        if (currentDB == null)
            loadDB();
        return currentDB;
    }

    private void loadDB() {
        currentDB = switch (appPrefs.dbType()) {
            case Undefined -> null;
            case Temporary -> new TemporaryDB();
            case SQL -> new SQLDB(appPrefs);
            default -> null;
        };
    }
}

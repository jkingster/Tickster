package io.jking.tickster.core;

import net.dv8tion.jda.api.utils.data.DataObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Config {

    private final String filePath;

    private final DataObject dataObject;

    public Config(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        this.dataObject = loadConfiguration();
    }

    private DataObject loadConfiguration() throws FileNotFoundException {
        return DataObject.fromJson(new BufferedReader(new FileReader(filePath)));
    }

    public String getString(String key) {
        return dataObject.getString(key, null);
    }

    public DataObject getObject(String key) {
        return dataObject.getObject(key);
    }

    public String getDBUrl() {
        return getObject("database").getString("url", null);
    }

    public String getDBUsername() {
        return getObject("database").getString("username", null);
    }

    public String getDBPassword() {
        return getObject("database").getString("password", null);
    }

}

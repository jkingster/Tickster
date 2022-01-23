package io.jking.tickster.core;

import net.dv8tion.jda.api.utils.data.DataObject;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

@NotThreadSafe
public class Config {

    private static final Config instance = new Config();

    private final String CONFIG_PATH = "config.json";

    private final DataObject dataObject;

    private Config() {
        this.dataObject = loadConfig();
    }

    public static Config getInstance() {
        if (instance == null)
            return new Config();
        return instance;
    }

    private DataObject loadConfig() {
        try {
            return DataObject.fromJson(new BufferedReader(new FileReader(CONFIG_PATH)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return DataObject.empty();
    }

    public DataObject getDataObject() {
        return dataObject;
    }

    public String getString(String key) {
        return getDataObject().getString(key, "");
    }

    public DataObject get(String key) {
        return getDataObject().getObject(key);
    }


}

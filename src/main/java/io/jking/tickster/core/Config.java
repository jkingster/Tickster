package io.jking.tickster.core;

import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.utils.Checks;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;


public class Config {

    private static final Config instance = new Config("config.json");

    private final DataObject data;

    private Config(String path) {
        this.data = getData(path);
    }

    private DataObject getData(String path) {
        Checks.notNull(path, "Config Path");
        try {
            return DataObject.fromJson(new BufferedReader(new FileReader(path)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return DataObject.empty();
        }
    }

    public DataObject getData() {
        return data;
    }

    public String getString(String key) {
        return getData().isNull(key) ? "" : getData().getString(key, null);
    }

    public DataObject getDataObject(String key) {
        return getData().getObject(key);
    }

    public DataArray getArray(String key) {
        return getData().getArray(key);
    }

    public static Config getInstance() {
        if (instance == null)
            return new Config("config.json");
        return instance;
    }

}

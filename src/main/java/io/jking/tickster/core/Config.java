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


}

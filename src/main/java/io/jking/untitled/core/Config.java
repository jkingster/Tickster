package io.jking.untitled.core;



import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.utils.Checks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Config {

    private final String filePath;

    private DataObject object;

    public Config(String filePath) throws IOException {
        Checks.notEmpty(filePath, "Config File Path");
        this.filePath = filePath;
        initialize();
    }

    private void initialize() throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            this.object = DataObject.fromJson(bufferedReader);
        }
    }

    public DataObject getObject(String key) {
        return object.getObject(key);
    }
}

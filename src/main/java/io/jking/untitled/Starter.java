package io.jking.untitled;

import io.jking.untitled.core.Config;
import io.jking.untitled.core.Untitled;

import java.util.Arrays;
import java.util.Comparator;

public class Starter {

    public static void main(String[] args) {
        try {
            Untitled.build(new Config("config.json"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

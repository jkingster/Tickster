package io.jking.tickster;

import io.jking.tickster.core.Tickster;
public class Starter {

    public static void main(String[] args) {
        try {
           new Tickster("config.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

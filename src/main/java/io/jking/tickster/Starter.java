package io.jking.tickster;

import io.jking.tickster.core.Tickster;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class Starter {

    public static void main(String[] args) {
        try {
           new Tickster("config.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

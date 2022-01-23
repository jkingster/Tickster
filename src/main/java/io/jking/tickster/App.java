package io.jking.tickster;

import io.jking.tickster.core.Tickster;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class App {

    public static void main(String[] args) {
        try {
            new Tickster().start();
        } catch (LoginException | IOException e) {
            e.printStackTrace();
        }
    }

}

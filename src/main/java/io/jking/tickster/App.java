package io.jking.tickster;

import io.jking.tickster.core.Config;
import io.jking.tickster.core.Tickster;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;

public class App {

    public static void main(String[] args)  {
        try {
            new Tickster(Config.getInstance()).start();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

}

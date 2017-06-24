package com.cassiomolin.example.chat.security;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cassiomolin on 22/06/2017.
 */
@ApplicationScoped
public class Authenticator {

    private static Map<String, String> users = new HashMap<>();

    static {
        users.put("joe", "secret");
        users.put("jane", "secret");
        users.put("john", "secret");
    }

    public boolean checkCredentials(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }
}

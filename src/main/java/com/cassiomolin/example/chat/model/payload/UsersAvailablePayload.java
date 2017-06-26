package com.cassiomolin.example.chat.model.payload;


import com.cassiomolin.example.chat.model.Payload;

import java.util.Set;

/**
 * Payload with details of the available users.
 *
 * @author cassiomolin
 */
public class UsersAvailablePayload implements Payload {

    public static final String TYPE = "usersAvailable";

    private Set<String> usernames;

    public UsersAvailablePayload() {

    }

    public Set<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(Set<String> usernames) {
        this.usernames = usernames;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}

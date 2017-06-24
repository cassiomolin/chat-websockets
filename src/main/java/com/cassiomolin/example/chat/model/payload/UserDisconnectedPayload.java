package com.cassiomolin.example.chat.model.payload;


import com.cassiomolin.example.chat.model.Payload;

public class UserDisconnectedPayload implements Payload {

    public static final String TYPE = "userDisconnected";

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}

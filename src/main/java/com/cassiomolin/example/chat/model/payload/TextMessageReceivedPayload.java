package com.cassiomolin.example.chat.model.payload;


import com.cassiomolin.example.chat.model.Payload;

public class TextMessageReceivedPayload implements Payload {

    public static final String TYPE = "textMessageReceived";

    private String username;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

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

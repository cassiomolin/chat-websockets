package com.cassiomolin.example.chat.model.payload;


import com.cassiomolin.example.chat.model.Payload;

/**
 * Represents the payload of a WebSocket frame to broadcast a text message.
 *
 * @author cassiomolin
 */
public class BroadcastTextMessagePayload implements Payload {

    public static final String TYPE = "broadcastTextMessage";

    private String username;
    private String content;

    public BroadcastTextMessagePayload() {

    }

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

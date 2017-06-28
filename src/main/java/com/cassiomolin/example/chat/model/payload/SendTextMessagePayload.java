package com.cassiomolin.example.chat.model.payload;


import com.cassiomolin.example.chat.model.Payload;

/**
 * Payload with details of a message sent by the client.
 *
 * @author cassiomolin
 */
public class SendTextMessagePayload implements Payload {

    public static final String TYPE = "sendTextMessage";

    private String content;

    public SendTextMessagePayload() {

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}

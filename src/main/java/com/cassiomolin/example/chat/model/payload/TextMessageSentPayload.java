package com.cassiomolin.example.chat.model.payload;


import com.cassiomolin.example.chat.model.Payload;

public class TextMessageSentPayload implements Payload {

    public static final String TYPE = "textMessageSent";

    private String content;

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

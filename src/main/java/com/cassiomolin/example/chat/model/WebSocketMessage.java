package com.cassiomolin.example.chat.model;

import com.cassiomolin.example.chat.model.payload.*;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * WebSocket message.
 * <p>
 * The <code>type<code/> field indicates the type of the <code>payload</code> field.
 *
 * @author cassiomolin
 */
public class WebSocketMessage {

    private String type;

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
            property = "type"
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = UserConnectedPayload.class, name = UserConnectedPayload.TYPE),
            @JsonSubTypes.Type(value = UserDisconnectedPayload.class, name = UserDisconnectedPayload.TYPE),
            @JsonSubTypes.Type(value = UsersAvailablePayload.class, name = UsersAvailablePayload.TYPE),
            @JsonSubTypes.Type(value = TextMessageSentPayload.class, name = TextMessageSentPayload.TYPE),
            @JsonSubTypes.Type(value = TextMessageReceivedPayload.class, name = TextMessageReceivedPayload.TYPE)
    })
    private Payload payload;

    public WebSocketMessage() {

    }

    public WebSocketMessage(Payload payload) {
        this.type = payload.getType();
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }
}

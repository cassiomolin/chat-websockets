package com.cassiomolin.example.chat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Interface that indicates a payload of a WebSocket message.
 *
 * @author cassiomolin
 */
public interface Payload {

    @JsonIgnore
    String getType();
}

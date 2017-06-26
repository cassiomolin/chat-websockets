package com.cassiomolin.example.chat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Interface that must be implemented by classes that represents the payload of a {@link WebSocketMessage}.
 *
 * @author cassiomolin
 */
public interface Payload {

    @JsonIgnore
    String getType();
}

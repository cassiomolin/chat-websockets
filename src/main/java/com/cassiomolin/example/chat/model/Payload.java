package com.cassiomolin.example.chat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Payload {

    @JsonIgnore
    String getType();
}

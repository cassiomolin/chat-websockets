package com.cassiomolin.example.chat.websocket.codec;

import com.cassiomolin.example.chat.model.WebSocketMessage;
import com.cassiomolin.example.chat.util.ObjectMapperFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Encoder for {@link WebSocketMessage}.
 *
 * @author cassiomolin
 */
public class MessageEncoder implements Encoder.Text<WebSocketMessage> {

    private final ObjectMapper mapper = ObjectMapperFactory.get();

    @Override
    public void init(EndpointConfig ec) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public String encode(WebSocketMessage message) throws EncodeException {
        try {
            return mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new EncodeException(message, e.getMessage(), e);
        }
    }
}
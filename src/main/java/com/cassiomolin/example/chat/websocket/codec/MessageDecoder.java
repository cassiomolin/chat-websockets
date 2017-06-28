package com.cassiomolin.example.chat.websocket.codec;

import com.cassiomolin.example.chat.model.WebSocketMessage;
import com.cassiomolin.example.chat.util.ObjectMapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;

/**
 * Decoder for {@link WebSocketMessage}.
 *
 * @author cassiomolin
 */
public class MessageDecoder implements Decoder.Text<WebSocketMessage> {

    private final ObjectMapper mapper = ObjectMapperFactory.get();

    @Override
    public void init(EndpointConfig ec) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public WebSocketMessage decode(String s) throws DecodeException {
        try {
            return mapper.readValue(s, WebSocketMessage.class);
        } catch (IOException e) {
            throw new DecodeException(s, e.getMessage(), e);
        }
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }
}
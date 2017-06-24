package com.cassiomolin.example.chat.endpoint;

import javax.enterprise.inject.spi.CDI;
import javax.websocket.server.ServerEndpointConfig;

public class CdiAwareConfigurator extends ServerEndpointConfig.Configurator {

    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        return CDI.current().select(endpointClass).get();
    }
}
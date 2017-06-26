package com.cassiomolin.example.chat.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * CDI producer for {@link ObjectMapper}.
 *
 * @author cassiomolin
 */
@ApplicationScoped
public class ObjectMapperProducer {

    @Produces
    public ObjectMapper produceObjectMapper() {
        return ObjectMapperFactory.get();
    }
}

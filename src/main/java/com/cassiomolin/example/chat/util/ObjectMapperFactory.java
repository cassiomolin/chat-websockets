package com.cassiomolin.example.chat.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Factory for Jackson's {@link ObjectMapper}.
 *
 * @author cassiomolin
 */
public class ObjectMapperFactory {

    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    private ObjectMapperFactory() {
        throw new AssertionError("No instances for you!");
    }

    public static ObjectMapper get() {
        return MAPPER;
    }
}
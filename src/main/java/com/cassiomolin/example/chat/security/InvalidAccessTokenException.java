package com.cassiomolin.example.chat.security;


/**
 * Thrown when an access token in invalid.
 *
 * @author cassiomolin
 */
public class InvalidAccessTokenException extends RuntimeException {

    public InvalidAccessTokenException(String message) {
        super(message);
    }

    public InvalidAccessTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}

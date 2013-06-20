package com.omrlnr.jreddit;

import java.io.IOException;

/**
 *
 * An exception indicating a rate limit has been reached. 
 * This could be caused by something like posting too frequently. 
 *
 */
public class RateLimitException extends IOException {

    public RateLimitException(String message) {
        super(message);
    }

    public RateLimitException(String message, Throwable cause) {
        super(message, cause);
    }
}

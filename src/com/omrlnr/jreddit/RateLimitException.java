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

    /**
     *
     * Returns time in seconds the rate limit error is asking
     *          for us to wait.
     *
     * @return time in seconds
     *
     */
    public int getRetryTime() {
        int ret = -1;

        //
        // Example messages to parse:
        // "you are doing that too much. try again in 9 minutes."
        //
        // "you are doing that too much. try again in 34 seconds."
        //
        String message = getMessage();

        String time = message.substring(
                            message.indexOf("try again in ") + 
                            "try again in ".length() );
        time = time.substring(0, time.indexOf(" "));

        try {
            ret = Integer.parseInt(time);
            if(message.indexOf("minutes") != -1) {
                // We need to wait minutes before retrying.
                ret = ret * 60;
            }
        } catch(NumberFormatException nfe) {

        }

        return ret;
    }

}

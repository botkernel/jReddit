package com.omrlnr.jreddit;

import java.io.IOException;

/**
 *
 * An exception indicating a user is banned from performing 
 * the requested operation. E.g. banned from posting in a subreddit.
 *
 */
public class BannedUserException extends IOException {

    public BannedUserException(String message) {
        super(message);
    }


}

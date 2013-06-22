package com.omrlnr.jreddit;

import java.io.IOException;

/**
 *
 * An exception indicating a comment we are replying to has been deleted.
 *
 */
public class DeletedCommentException extends IOException {

    public DeletedCommentException(String message) {
        super(message);
    }


}

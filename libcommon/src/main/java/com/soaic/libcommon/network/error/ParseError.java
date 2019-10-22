package com.soaic.libcommon.network.error;

/**
 * Indicates that the server responded with an error response.
 */
public class ParseError extends Exception {

    public ParseError(Throwable throwable) {
        super("ParseError",throwable);
    }

    public ParseError() {
        super();
    }

}


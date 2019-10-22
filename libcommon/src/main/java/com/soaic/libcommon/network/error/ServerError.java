package com.soaic.libcommon.network.error;

/**
 * Indicates that the server responded with an error response.
 */
public class ServerError extends Exception {

    public ServerError(String message) {
        super("ServerError: " + message);
    }

    public ServerError(Throwable throwable) {
        super("ServerError", throwable);
    }

    public ServerError() {
        super();
    }

}


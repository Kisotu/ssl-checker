package com.jake.sslchecker.exception;

public class SSLCheckException extends RuntimeException {
    public SSLCheckException(String message) {
        super(message);
    }

    public SSLCheckException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.likelion.news.exception;

public class UnAuthorizedException extends ClientException{

    public UnAuthorizedException(String message) {
        super(message);
    }

    public UnAuthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}

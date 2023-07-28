package com.likelion.news.exception;

public class UnAuthorizedExcpetion extends ClientException{

    public UnAuthorizedExcpetion(String message) {
        super(message);
    }

    public UnAuthorizedExcpetion(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.likelion.news.exception;

public class DuplicateException extends RuntimeException{
    public DuplicateException(String message) {
        super(message);
    }

    public static class DuplicateExpertInfoException extends DuplicateException {
        public DuplicateExpertInfoException() {
            super("이미 신청한 전문가 신청이 있습니다. ");
        }
    }
}

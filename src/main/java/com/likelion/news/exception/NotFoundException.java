package com.likelion.news.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) { super(message); }

    public static class NoUserException extends NotFoundException {
        public NoUserException() {
            super("사용자를 찾을 수 없습니다.");
        }
    }

    public static class NoNewsException extends NotFoundException {
        public NoNewsException() {
            super("해당 뉴스를 찾을 수 없습니다.");
        }
    }

    public static class NoCommentException extends NotFoundException {
        public NoCommentException() {
            super("해당 댓글을 찾을 수 없습니다.");
        }
    }
}

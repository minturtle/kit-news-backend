package com.likelion.news.entity.enums;

public enum LoginType {
    KAKAO("kakao");

    private final String text;

    LoginType(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static LoginType fromString(String text) {
        for (LoginType loginType : LoginType.values()) {
            if (loginType.text.equalsIgnoreCase(text)) {
                return loginType;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}

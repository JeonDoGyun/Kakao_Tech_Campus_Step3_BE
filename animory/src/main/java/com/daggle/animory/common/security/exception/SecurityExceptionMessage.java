package com.daggle.animory.common.security.exception;

public enum SecurityExceptionMessage {
    UNAUTHORIZED("인증되지 않은 사용자입니다."),
    FORBIDDEN("권한이 없습니다."),
    INVALID_TOKEN("유효하지 않은 토큰입니다."),
    INVALID_TOKEN_FORMAT("유효하지 않은 토큰 형식입니다."),;

    private final String message;

    SecurityExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

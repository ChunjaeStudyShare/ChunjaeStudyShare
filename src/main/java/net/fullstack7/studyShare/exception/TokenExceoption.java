package net.fullstack7.studyShare.exception;

import lombok.Getter;
@Getter
public class TokenExceoption extends RuntimeException {
    private final String errorCode;
    private final int status;

    public TokenExceoption(String message, String errorCode, int status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    public static TokenExceoption tokenExpired() {
        return new TokenExceoption("토큰이 만료되었습니다.", "AUTH002", 401);
    }
}

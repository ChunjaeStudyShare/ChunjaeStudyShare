package net.fullstack7.studyShare.exception;

import lombok.Getter;
@Getter
public class TokenException extends RuntimeException {

    public TokenException(String message) {
        super(message);
    }
}

package net.fullstack7.studyShare.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    
    public CustomException(String message) {
        super(message);
    }
}

package kr.co.ync.projectA.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {
    private int status;
    private String message;

    public CustomException(String message) {
        super(message);
        this.status = 400; // 기본 BAD_REQUEST
        this.message = message;
    }
}

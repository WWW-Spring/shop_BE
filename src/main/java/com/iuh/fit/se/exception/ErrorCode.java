package com.iuh.fit.se.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    FULLNAME_REQUIRED(1003, "Full name is required", HttpStatus.BAD_REQUEST),
    PASSWORD_REQUIRED(1004, "Password is required", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1005, "Password must be at least 6 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_MISMATCH(1006, "Password and Confirm Password do not match", HttpStatus.BAD_REQUEST),
    CONFIRM_PASSWORD_REQUIRED(1007, "Confirm password is required", HttpStatus.BAD_REQUEST),
    PHONE_INVALID(1008, "Phone number format is invalid", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatus statusCode;
}

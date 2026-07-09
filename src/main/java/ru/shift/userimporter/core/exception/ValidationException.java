package ru.shift.userimporter.core.exception;

import lombok.Getter;
import ru.shift.userimporter.core.model.ErrorCode;

@Getter
public class ValidationException extends RuntimeException{
    private ErrorCode errorCode;
    public ValidationException(String message){
        super(message);
    }
    public ValidationException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}

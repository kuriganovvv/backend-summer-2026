package ru.shift.userimporter.api.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.shift.userimporter.core.exception.FileAlreadyExistsException;
import ru.shift.userimporter.core.exception.ResourseNotFountException;
import ru.shift.userimporter.core.exception.ValidationException;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
@SuppressWarnings("unused")
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourseNotFountException.class)
    public ResponseEntity<Map<String,String>> handleNotFound(ResourseNotFountException e){
        return errorResponse(e.getMessage(),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(FileAlreadyExistsException.class)
    public ResponseEntity<Map<String,String>> handleConflict(FileAlreadyExistsException e){
        return errorResponse(e.getMessage(),HttpStatus.CONFLICT);
    }
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidation(ValidationException e){
        return errorResponse(e.getMessage(),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String,String>> handleRuntimeException(RuntimeException e){
        return errorResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    private ResponseEntity<Map<String,String>> errorResponse(String message, HttpStatus status){
        Map<String,String> error = new HashMap<>();
        error.put("message",message);
        return ResponseEntity.status(status).body(error);
    }
}

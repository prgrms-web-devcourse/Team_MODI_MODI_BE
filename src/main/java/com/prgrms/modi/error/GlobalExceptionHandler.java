package com.prgrms.modi.error;

import com.prgrms.modi.error.exception.AlreadyJoinedException;
import com.prgrms.modi.error.exception.ForbiddenException;
import com.prgrms.modi.error.exception.InvalidAuthenticationException;
import com.prgrms.modi.error.exception.NotEnoughPartyCapacityException;
import com.prgrms.modi.error.exception.NotEnoughPointException;
import com.prgrms.modi.error.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> internalServerErrorHandler(Exception e) {
        return ResponseEntity
            .internalServerError()
            .body(new ErrorResponse(e.getMessage(), "", HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
        IllegalStateException.class, IllegalArgumentException.class,
        NotEnoughPointException.class, NotEnoughPartyCapacityException.class,
        AlreadyJoinedException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestException(Exception e) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(e.getMessage(), "", HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundExceptionHandler(NotFoundException e) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(e.getMessage(), "", HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> forbiddenExceptionHandler(ForbiddenException e) {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(new ErrorResponse(e.getMessage(), "", HttpStatus.FORBIDDEN));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> invalidRequestHandler(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors()
            .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> invalidConstraintHandler(ConstraintViolationException e) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(e.getMessage(), "", HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(InvalidAuthenticationException.class)
    public ResponseEntity<ErrorResponse> invalidAuthenticationHandler(InvalidAuthenticationException e) {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(new ErrorResponse(e.getMessage(), "", HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> missingParamHandler(MissingServletRequestParameterException e) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(e.getMessage(), "", HttpStatus.BAD_REQUEST));
    }

}

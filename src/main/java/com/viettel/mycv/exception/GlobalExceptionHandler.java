package com.viettel.mycv.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.viettel.mycv.dto.response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse ResourceNotFoundHandler(ResourceNotFound e, WebRequest request) {
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setPath(request.getDescription(false).replace("uri=", ""));
        response.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        response.setMessage(e.getMessage());

        return response;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse ConstraintViolationExceptionHandler(ConstraintViolationException e, WebRequest request) {
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setPath(request.getDescription(false).replace("uri=", ""));
        response.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        String message = e.getMessage();
        message = message.substring(message.lastIndexOf(':') + 1);
        response.setMessage(message);
        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e, WebRequest request) {
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setPath(request.getDescription(false).replace("uri=", ""));
        response.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        String message = e.getMessage();
        int firtIndex = message.lastIndexOf('[');
        int lastIndex = message.lastIndexOf(']') - 1;
        message = message.substring(firtIndex + 1, lastIndex);
        response.setMessage(message);
        return response;
    }

    @ExceptionHandler({InvalidFormatException.class, MismatchedInputException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse HttpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e, WebRequest request) {
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setPath(request.getDescription(false).replace("uri=", ""));
        response.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        response.setMessage("Failing to parse the field in request");
        return response;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse DataIntegrityViolationExceptionHandler(DataIntegrityViolationException e, WebRequest request) {
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setPath(request.getDescription(false).replace("uri=", ""));
        response.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        String message = e.getMessage();
        int firstIndex = message.indexOf("[") + 1;
        int lastIndex = message.indexOf("]");
        message = message.substring(firstIndex, lastIndex);
        response.setMessage(message);
        return response;
    }

    @ExceptionHandler(UserNotVerifiedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse UserNotVerifiedExceptionHandler(UserNotVerifiedException e, WebRequest request) {
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setPath(request.getDescription(false).replace("uri=", ""));
        response.setError(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        response.setMessage(e.getMessage());
        return response;
    }

    @ExceptionHandler(UnauthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse UnauthenticationExceptionHandler(UnauthenticationException e, WebRequest request) {
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setPath(request.getDescription(false).replace("uri=", ""));
        response.setError(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        response.setMessage(e.getMessage());
        return response;
    }
}

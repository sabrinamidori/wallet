package com.ontop.assessment.payment.infrastructure.config.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {ResourceNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorDetails resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return new ErrorDetails(ex.getCode().getCode(), ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler(value = {InvalidOperationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDetails invalidAmountException(InvalidOperationException ex, WebRequest request) {
        return new ErrorDetails(ex.getCode().getCode(), ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDetails constraintViolationException(ConstraintViolationException ex) {
        List<String> errors = new ArrayList<>() ;
        for (var violation : ex.getConstraintViolations()) {
            errors.add(String.format("%s: %s",violation.getPropertyPath().toString(), violation.getMessage()));
        }
        return new ErrorDetails(ErrorsCode.INVALID_BODY.getCode(), String.join(" ,", errors),
                ex.getMessage());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDetails validationException(ErrorResponse ex) {
        List<String> errors = new ArrayList<>() ;
        for (var violation : Objects.requireNonNull(ex.getDetailMessageArguments())) {
            errors.add(violation.toString());
        }
        return new ErrorDetails(ErrorsCode.INVALID_BODY.getCode(),
                String.join(" ,", errors), ex.getBody().getDetail());
    }

    @ExceptionHandler(value = {UnprocessableEntityException.class})
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorDetails unprocessedEntityException(UnprocessableEntityException ex, WebRequest request) {
        return new ErrorDetails(ex.getCode().getCode(), ex.getMessage(), request.getDescription(false));
    }
}

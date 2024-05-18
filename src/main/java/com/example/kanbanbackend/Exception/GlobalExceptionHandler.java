package com.example.kanbanbackend.Exception;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseEntity<ErrorValidationResponse> handleHandlerMethodValidationException(HandlerMethodValidationException exception, WebRequest request) {
//        ErrorValidationResponse errorResponse = new ErrorValidationResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Validation error. Check 'errors' field for details.", request.getDescription(false));
//        List<ParameterValidationResult> paramNames = exception.getAllValidationResults();
//        for (ParameterValidationResult param : paramNames) {
//            errorResponse.addValidationError(param.getMethodParameter().getParameterName(), param.getResolvableErrors().get(0).getDefaultMessage() + " (" + param.getArgument().toString() + ")" );
//        }
//        return ResponseEntity.unprocessableEntity().body(errorResponse);
////        return buildErrorResponse(null, exception, exception.getMethod().getName(), HttpStatus.valueOf(exception.getStatusCode().value()), request);
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorValidationResponse> handleHandlerMethodValidationException(MethodArgumentNotValidException exception, WebRequest request) {
        ErrorValidationResponse errorResponse = new ErrorValidationResponse(HttpStatus.BAD_REQUEST.value(), "Validation error. Check 'errors' field for details. taskForCreatedOrUpdated", request.getDescription(false));

        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(),fieldError.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
//        return buildErrorResponse(null, exception, exception.getMethod().getName(), HttpStatus.valueOf(exception.getStatusCode().value()), request);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleItemNotFoundException(ItemNotFoundException ex, HttpServletRequest request) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        ErrorResponse errorResponse = new ErrorResponse(timestamp, HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    @ExceptionHandler(ItemNotFoundDelUpdate.class)
    public ResponseEntity< delException> handleItemNotFoundExceptionForDelete(ItemNotFoundDelUpdate ex, HttpServletRequest request) {
        delException errorResponse = new delException(Timestamp.from(Instant.now()), HttpStatus.NOT_FOUND.value(), ex.getMessage() , request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex,HttpServletRequest request){
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        ErrorResponse errorResponse = new ErrorResponse(timestamp, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}



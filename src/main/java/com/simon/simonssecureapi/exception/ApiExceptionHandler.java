package com.simon.simonssecureapi.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        Map<String, Object> response = buildResponse(
                HttpStatus.METHOD_NOT_ALLOWED,
                "Method not allowed",
                "This endpoint does not support " + e.getMethod(),
                request);

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationError(MethodArgumentNotValidException e, HttpServletRequest request){
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage()));

        Map<String, Object> response = buildResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid input data",
                "Validation failed for one or more fields",
                request);
        response.put("affected fields", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleMemberNotFound(MemberNotFoundException e, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildResponse(HttpStatus.NOT_FOUND, "Not found", e.getMessage(), request));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AuthorizationDeniedException e, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(buildResponse(HttpStatus.FORBIDDEN, "Forbidden access", "You do not have the right authorization", request));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(UsernameNotFoundException e, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildResponse(HttpStatus.NOT_FOUND, "User not found", e.getMessage(), request));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException e, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildResponse(HttpStatus.BAD_REQUEST, "Bad request", e.getMessage(), request));
    }



    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(
            org.springframework.dao.DataIntegrityViolationException e,
            jakarta.servlet.http.HttpServletRequest request) {

        String message = "Database conflict: The record already exists or violates a database constraint.";

        if (e.getRootCause() != null && e.getRootCause().getMessage().contains("Duplicate entry")) {
            message = "Duplicate entry: The information you provided is already registered in our system.";
        }

        Map<String, Object> response = buildResponse(
                HttpStatus.CONFLICT,
                "Conflict",
                message,
                request);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleResourceExists(ResourceAlreadyExistsException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildResponse(
                        HttpStatus.CONFLICT,
                        "Conflict",
                        e.getMessage(),
                        request));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoHandlerFound(
            org.springframework.web.servlet.NoHandlerFoundException e,
            jakarta.servlet.http.HttpServletRequest request) {

        String message = "The path '" + e.getRequestURL() + "' does not exist on this server.";

        Map<String, Object> response = buildResponse(
                HttpStatus.NOT_FOUND,
                "Not Found",
                message,
                request);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleJsonErrors(
            HttpMessageNotReadableException e, HttpServletRequest request) {

        String details = "Malformed JSON request";
        Throwable cause = e.getCause();

        if (cause instanceof InvalidFormatException ife) {
            if (ife.getTargetType().isEnum()) {
                details = String.format("Invalid value '%s'. Accepted values are: %s",
                        ife.getValue(),
                        Arrays.toString(ife.getTargetType().getEnumConstants()));
            } else {
                details = "Invalid data type for field: " + ife.getPath().get(0).getFieldName();
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildResponse(HttpStatus.BAD_REQUEST, "Bad Request", details, request));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception e, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", e.getMessage(), request));
    }

    private static Map<String, Object> buildResponse(HttpStatus status, String error, String message, HttpServletRequest request) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("status", status.value());
        response.put("error", error);
        response.put("message", message);
        response.put("path", request.getMethod() + " " + request.getRequestURI());
        return response;
    }
}
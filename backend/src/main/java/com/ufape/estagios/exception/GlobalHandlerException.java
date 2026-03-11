package com.ufape.estagios.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;


@RestControllerAdvice
public class GlobalHandlerException {
	
	@ExceptionHandler(IdNotFoundException.class)
	public ResponseEntity<String> handlerIdNotFoundException(IdNotFoundException ex){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<String> handlerAccessDeniedException(AccessDeniedException ex){
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
	}
	
	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<String> handlerEmailAlreadyExistsException(EmailAlreadyExistsException ex){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<String> handlerBadCredentialsException(BadCredentialsException ex){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		Map<String, String> errors = new HashMap<>();
		
		e.getBindingResult().getFieldErrors().forEach(error -> {
			errors.put(error.getField(), error.getDefaultMessage());
		});
		
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map<String, String>> handleConstraintViolationException(
	        ConstraintViolationException e) {

	    Map<String, String> errors = new HashMap<>();

	    e.getConstraintViolations().forEach(violation -> {
	        String field = violation.getPropertyPath().toString();
	        String message = violation.getMessage();
	        errors.put(field, message);
	    });

	    return ResponseEntity.badRequest().body(errors);
	}
	
	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<String> handlerConflictException(ConflictException ex){
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}
	
}

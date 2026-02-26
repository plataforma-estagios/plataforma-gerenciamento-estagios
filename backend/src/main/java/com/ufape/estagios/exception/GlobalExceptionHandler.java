package com.ufape.estagios.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(IdNotFoundException.class)
	public ResponseEntity<String> handlerIdNotFoundException(IdNotFoundException ex){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<String> handlerAcessDeniedException(AccessDeniedException ex){
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
	}
}

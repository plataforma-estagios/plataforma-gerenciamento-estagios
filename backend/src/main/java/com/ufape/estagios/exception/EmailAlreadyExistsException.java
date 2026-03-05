package com.ufape.estagios.exception;

public class EmailAlreadyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmailAlreadyExistsException() {
		super("Email already exists!");
	}
	
	
}

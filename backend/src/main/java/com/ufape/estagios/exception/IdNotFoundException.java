package com.ufape.estagios.exception;

public class IdNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IdNotFoundException(String entity) {
		super(String.format("Id of %s not found", entity));
	}
	
	
}

package com.music.authenticationMS.exception;

@SuppressWarnings("serial")
public class NotFoundException extends Exception {
	
	public NotFoundException() {
		super("No authenticated user available");
	}
	
	public NotFoundException(String entity, Integer id) {
		super(String.format("The entity %s with id %s doesn't exist", entity, id));
	}
	
	public NotFoundException(String entity, String email) {
		super(String.format("The entity %s with email %s doesn't exist", entity, email));
	}
}

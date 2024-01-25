package com.music.userMS.exception;

@SuppressWarnings("serial")
public class NotFoundException extends Exception {
	
	public NotFoundException() {
		super("No authenticated user available");
	}
	
	public NotFoundException(String entity, int id) {
		super(String.format("The entity %s with id %s doesn't exist", entity, id));
	}
	
	public NotFoundException(String entity, String name) {
		super(String.format("The entity %s with name %s doesn't exist", entity, name));
	}
}

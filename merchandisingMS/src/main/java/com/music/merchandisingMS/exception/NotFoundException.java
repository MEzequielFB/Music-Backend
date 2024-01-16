package com.music.merchandisingMS.exception;

@SuppressWarnings("serial")
public class NotFoundException extends Exception {
	public NotFoundException(String entity, int id) {
		super(String.format("The entity %s with id %s doesn't exist", entity, id));
	}
	
	public NotFoundException(String entity, String name) {
		super(String.format("The entity %s with the name %s doesn't exist", entity, name));
	}
}

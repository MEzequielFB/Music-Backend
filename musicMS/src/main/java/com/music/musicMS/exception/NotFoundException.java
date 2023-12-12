package com.music.musicMS.exception;

@SuppressWarnings("serial")
public class NotFoundException extends Exception {
	public NotFoundException(String entity, int id) {
		super(String.format("The entity %s with id %s doesn't exist", entity, id));
	}
}

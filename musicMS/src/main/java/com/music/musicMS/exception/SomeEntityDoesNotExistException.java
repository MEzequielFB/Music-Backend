package com.music.musicMS.exception;

@SuppressWarnings("serial")
public class SomeEntityDoesNotExistException extends Exception {
	public SomeEntityDoesNotExistException(String entity) {
		super(String.format("Some of the %s passed as an argument doesn't exist", entity));
	}
}

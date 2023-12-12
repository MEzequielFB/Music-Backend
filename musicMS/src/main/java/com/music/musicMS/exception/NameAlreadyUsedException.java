package com.music.musicMS.exception;

@SuppressWarnings("serial")
public class NameAlreadyUsedException extends Exception {
	public NameAlreadyUsedException(String entity, String name) {
		super(String.format("a %s with the name '%s' already exists", entity, name));
	}
}

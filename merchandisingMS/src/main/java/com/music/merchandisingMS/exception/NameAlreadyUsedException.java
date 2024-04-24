package com.music.merchandisingMS.exception;

@SuppressWarnings("serial")
public class NameAlreadyUsedException extends Exception {
	public NameAlreadyUsedException(String entity, String name) {
		super(String.format("A %s with the name '%s' already exists", entity, name));
	}
}

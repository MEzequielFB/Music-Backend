package com.music.backend.exception;

@SuppressWarnings("serial")
public class EmailAlreadyUsedException extends Exception {
	public EmailAlreadyUsedException(String email) {
		super(String.format("the email '%s' is already in use", email));
	}
}

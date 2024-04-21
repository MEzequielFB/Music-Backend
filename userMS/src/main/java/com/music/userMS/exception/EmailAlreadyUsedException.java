package com.music.userMS.exception;

@SuppressWarnings("serial")
public class EmailAlreadyUsedException extends Exception {
	public EmailAlreadyUsedException(String email) {
		super(String.format("The email '%s' is already in use", email));
	}
}

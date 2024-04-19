package com.music.authenticationMS.exception;

@SuppressWarnings("serial")
public class AuthenticationException extends Exception {

	public AuthenticationException() {
		super("Invalid email or password");
	}
}

package com.music.MusicAPIGateway.exception;

@SuppressWarnings("serial")
public class InvalidTokenException extends Exception {

	public InvalidTokenException() {
		super("Invalid token!");
	}
}

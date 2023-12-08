package com.music.backend.exception;

@SuppressWarnings("serial")
public class AlreadyContainsSongException extends Exception {
	public AlreadyContainsSongException(String entity) {
		super(String.format("The entity %s already contains the song", entity));
	}
}

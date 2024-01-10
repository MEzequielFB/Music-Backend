package com.music.merchandisingMS.exception;

@SuppressWarnings("serial")
public class DeletedEntityException extends Exception {
	public DeletedEntityException(String entity, String name) {
		super(String.format("The entity %s with name '%s' is deleted", entity, name));
	}
}

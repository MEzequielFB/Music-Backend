package com.music.merchandisingMS.exception;

@SuppressWarnings("serial")
public class EntityWithUserIdAlreadyUsedException extends Exception {
	public EntityWithUserIdAlreadyUsedException(String entity, Integer userId) {
		super(String.format("Cannot create the entity %s because another one is already referencing the userId %s", entity, userId));
	}
}

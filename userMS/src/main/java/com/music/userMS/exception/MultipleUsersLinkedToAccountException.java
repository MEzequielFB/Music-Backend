package com.music.userMS.exception;

@SuppressWarnings("serial")
public class MultipleUsersLinkedToAccountException extends Exception {
	public MultipleUsersLinkedToAccountException(Integer accountId) {
		super(String.format("Cannot delete the account with id %s because it has multiple users linked", accountId));
	}
}

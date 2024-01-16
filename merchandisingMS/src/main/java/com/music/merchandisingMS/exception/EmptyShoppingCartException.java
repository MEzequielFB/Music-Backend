package com.music.merchandisingMS.exception;

@SuppressWarnings("serial")
public class EmptyShoppingCartException extends Exception {
	public EmptyShoppingCartException(Integer id) {
		super(String.format("The shopping cart with id %s is empty", id));
	}
}

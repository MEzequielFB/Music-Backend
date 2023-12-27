package com.music.musicMS.exception;

@SuppressWarnings("serial")
public class DoNotContainsTheSongException extends Exception {
	public DoNotContainsTheSongException(String albumName, String songName) {
		super(String.format("The album %s do not contains th song %s", albumName, songName));
	}
}

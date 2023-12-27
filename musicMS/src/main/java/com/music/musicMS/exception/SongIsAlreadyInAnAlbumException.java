package com.music.musicMS.exception;

@SuppressWarnings("serial")
public class SongIsAlreadyInAnAlbumException extends Exception {
	public SongIsAlreadyInAnAlbumException(String songName) {
		super(String.format("The song %s is already in an album", songName));
	}
}

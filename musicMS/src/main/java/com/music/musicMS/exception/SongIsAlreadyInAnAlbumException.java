package com.music.musicMS.exception;

@SuppressWarnings("serial")
public class SongIsAlreadyInAnAlbumException extends Exception {
	public SongIsAlreadyInAnAlbumException(String songName, String albumName) {
		super(String.format("The song '%s' is already in an album ('%s')", songName, albumName));
	}
}

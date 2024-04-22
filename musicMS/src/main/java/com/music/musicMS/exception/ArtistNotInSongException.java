package com.music.musicMS.exception;

@SuppressWarnings("serial")
public class ArtistNotInSongException extends Exception {

	public ArtistNotInSongException() {
		super("Can't save the song because the logged artist is not part of the song");
	}
}

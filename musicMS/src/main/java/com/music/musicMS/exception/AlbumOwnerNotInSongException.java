package com.music.musicMS.exception;

@SuppressWarnings("serial")
public class AlbumOwnerNotInSongException extends Exception {
	public AlbumOwnerNotInSongException(String songName, String artistName) {
		super(String.format("Can't add the song to the album because the artist '%s' is not an author of the song '%s'", artistName, songName));
	}
}

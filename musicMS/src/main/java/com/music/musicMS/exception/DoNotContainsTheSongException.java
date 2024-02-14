package com.music.musicMS.exception;

import com.music.musicMS.model.Album;
import com.music.musicMS.model.Playlist;

@SuppressWarnings("serial")
public class DoNotContainsTheSongException extends Exception {
	public DoNotContainsTheSongException(Album album, String songName) {
		super(String.format("The album '%s' do not contains the song '%s'", album.getName(), songName));
	}
	
	public DoNotContainsTheSongException(Playlist playlist, String songName) {
		super(String.format("The playlist '%s' do not contains the song '%s'", playlist.getName(), songName));
	}
}

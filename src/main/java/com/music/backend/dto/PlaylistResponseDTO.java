package com.music.backend.dto;

import com.music.backend.model.Playlist;
import com.music.backend.model.User;

import lombok.Data;

@Data
public class PlaylistResponseDTO {
	private int id;
	private String name;
	private boolean isPublic;
	private User user;
	
	public PlaylistResponseDTO(Playlist playlist) {
		this.id = playlist.getId();
		this.name = playlist.getName();
		this.isPublic = playlist.isPublic();
		this.user = playlist.getUser();
	}
}

package com.music.musicMS.dto;

import com.music.musicMS.model.Playlist;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlaylistResponseDTO {
	private int id;
	private String name;
	private boolean isPublic;
	private UserResponseDTO user;
	
	public PlaylistResponseDTO(Playlist playlist, UserResponseDTO user) {
		this.id = playlist.getId();
		this.name = playlist.getName();
		this.isPublic = playlist.isPublic();
		this.user = user;
	}
}

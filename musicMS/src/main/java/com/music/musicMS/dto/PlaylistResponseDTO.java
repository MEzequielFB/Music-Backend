package com.music.musicMS.dto;

import com.music.musicMS.model.Playlist;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlaylistResponseDTO {
	private Integer id;
	private String name;
	private Boolean isPublic;
	private UserDTO user;
	
	public PlaylistResponseDTO(Playlist playlist) {
		this.id = playlist.getId();
		this.name = playlist.getName();
		this.isPublic = playlist.isPublic();
		this.user = playlist.getUser();
	}
}

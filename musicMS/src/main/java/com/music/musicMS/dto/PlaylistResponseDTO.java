package com.music.musicMS.dto;

import com.music.musicMS.model.Playlist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistResponseDTO {
	private Integer id;
	private String name;
	private Boolean isPublic;
	private UserDTO user;
	
	public PlaylistResponseDTO(Playlist playlist, UserDTO user) {
		this.id = playlist.getId();
		this.name = playlist.getName();
		this.isPublic = playlist.getIsPublic();
		this.user = user;
	}
}

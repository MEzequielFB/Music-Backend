package com.music.musicMS.dto;

import com.music.musicMS.model.Playlist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistResponseDTO {
	@Schema(name = "id", example = "1")
	private Integer id;
	
	@Schema(name = "name", example = "playlist1")
	private String name;
	
	@Schema(name = "isPublic", example = "true")
	private Boolean isPublic;
	
	@Schema(implementation = UserDTO.class)
	private UserDTO user;
	
	public PlaylistResponseDTO(Playlist playlist, UserDTO user) {
		this.id = playlist.getId();
		this.name = playlist.getName();
		this.isPublic = playlist.getIsPublic();
		this.user = user;
	}
}

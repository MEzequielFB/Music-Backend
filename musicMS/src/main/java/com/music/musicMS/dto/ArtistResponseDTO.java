package com.music.musicMS.dto;

import com.music.musicMS.model.Artist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistResponseDTO {
	@Schema(name = "id", example = "1")
	private Integer id;
	
	@Schema(name = "userId", example = "1")
	private Integer userId;
	
	@Schema(name = "name", example = "artist1")
	private String name;
	
	public ArtistResponseDTO(Artist artist) {
		this.id = artist.getId();
		this.userId = artist.getUserId();
		this.name = artist.getName();
	}
}

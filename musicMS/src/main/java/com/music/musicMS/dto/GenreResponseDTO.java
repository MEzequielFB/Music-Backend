package com.music.musicMS.dto;

import com.music.musicMS.model.Genre;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenreResponseDTO {
	@Schema(name = "id", example = "1")
	private Integer id;
	
	@Schema(name = "name", example = "genre1")
	private String name;
	
	public GenreResponseDTO(Genre genre) {
		this.id = genre.getId();
		this.name = genre.getName();
	}
}

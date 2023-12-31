package com.music.musicMS.dto;

import com.music.musicMS.model.Genre;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenreResponseDTO {
	private int id;
	private String name;
	
	public GenreResponseDTO(Genre genre) {
		this.id = genre.getId();
		this.name = genre.getName();
	}
}

package com.music.musicMS.dto;

import com.music.musicMS.model.Genre;

import lombok.Data;

@Data
public class GenreResponseDTO {
	private int id;
	private String name;
	
	public GenreResponseDTO(Genre genre) {
		this.id = genre.getId();
		this.name = genre.getName();
	}
}

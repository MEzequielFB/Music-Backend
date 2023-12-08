package com.music.backend.dto;

import com.music.backend.model.Artist;

import lombok.Data;

@Data
public class ArtistResponseDTO {
	private int id;
	private String name;
	
	public ArtistResponseDTO(Artist artist) {
		this.id = artist.getId();
		this.name = artist.getName();
	}
}

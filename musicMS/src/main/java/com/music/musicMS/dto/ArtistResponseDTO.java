package com.music.musicMS.dto;

import com.music.musicMS.model.Artist;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArtistResponseDTO {
	private int id;
	private String name;
	
	public ArtistResponseDTO(Artist artist) {
		this.id = artist.getId();
		this.name = artist.getName();
	}
}

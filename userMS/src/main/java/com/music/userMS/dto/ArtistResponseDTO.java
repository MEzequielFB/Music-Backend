package com.music.userMS.dto;

import com.music.userMS.model.Artist;

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

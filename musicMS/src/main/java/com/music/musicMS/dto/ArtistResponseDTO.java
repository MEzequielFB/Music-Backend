package com.music.musicMS.dto;

import com.music.musicMS.model.Artist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistResponseDTO {
	private Integer id;
	private Integer userId;
	private String name;
	
	public ArtistResponseDTO(Artist artist) {
		this.id = artist.getId();
		this.userId = artist.getUserId();
		this.name = artist.getName();
	}
}

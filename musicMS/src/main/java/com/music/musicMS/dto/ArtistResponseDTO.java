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
	private int id;
	private String name;
	private Boolean isDeleted;
	private Integer userId;
	
	public ArtistResponseDTO(Artist artist) {
		this.id = artist.getId();
		this.name = artist.getName();
		this.isDeleted = artist.getIsDeleted();
		this.userId = artist.getUserId();
	}
}

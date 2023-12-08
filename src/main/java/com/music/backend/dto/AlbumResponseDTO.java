package com.music.backend.dto;

import java.util.List;

import com.music.backend.model.Album;
import com.music.backend.model.Artist;

import lombok.Data;

@Data
public class AlbumResponseDTO {
	private int id;
	private String name;
	private List<Artist> artists;
	
	public AlbumResponseDTO(Album album) {
		this.id = album.getId();
		this.name = album.getName();
		this.artists = album.getArtists();
	}
}

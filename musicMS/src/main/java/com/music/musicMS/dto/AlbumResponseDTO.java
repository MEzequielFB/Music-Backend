package com.music.musicMS.dto;

import java.util.List;

import com.music.musicMS.model.Album;

import lombok.Data;

@Data
public class AlbumResponseDTO {
	private int id;
	private String name;
	private List<ArtistResponseDTO> artists;
	private List<SongResponseDTO> songs;
	
	public AlbumResponseDTO(Album album) {
		this.id = album.getId();
		this.name = album.getName();
		this.artists = album.getArtists().stream().map( ArtistResponseDTO::new ).toList();
		this.songs = album.getSongs().stream().map( SongResponseDTO::new ).toList();
	}
}

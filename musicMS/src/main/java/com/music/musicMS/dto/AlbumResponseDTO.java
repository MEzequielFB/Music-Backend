package com.music.musicMS.dto;

import java.util.List;

import com.music.musicMS.model.Album;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumResponseDTO {
	private int id;
	private String name;
	private ArtistResponseDTO owner;
	private List<ArtistResponseDTO> artists;
	private List<SongResponseDTO> songs;
	
	public AlbumResponseDTO(Album album) {
		this.id = album.getId();
		this.name = album.getName();
		this.owner = new ArtistResponseDTO(album.getOwner());
		this.artists = album.getArtists().stream().map( ArtistResponseDTO::new ).toList();
		this.songs = album.getSongs().stream().map( SongResponseDTO::new ).toList();
	}
}

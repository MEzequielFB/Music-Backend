package com.music.backend.dto;

import java.util.List;

import com.music.backend.model.Artist;
import com.music.backend.model.Genre;
import com.music.backend.model.Song;

import lombok.Data;

@Data
public class SongResponseDTO {
	private int id;
	private String name;
	private List<ArtistResponseDTO> artists;
	private List<GenreResponseDTO> genres;
	
	public SongResponseDTO(Song song) {
		this.id = song.getId();
		this.name = song.getName();
		this.artists = song.getArtists().stream().map(ArtistResponseDTO::new).toList();
		this.genres = song.getGenres().stream().map(GenreResponseDTO::new).toList();
	}
}

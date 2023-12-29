package com.music.musicMS.dto;

import java.util.List;

import com.music.musicMS.model.Song;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

package com.music.musicMS.dto;

import java.util.List;

import com.music.musicMS.model.Song;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongResponseDTO {
	private int id;
	private String name;
	private Integer reproductions;
	private Integer duration; // seconds
	private String album;
	private List<ArtistResponseDTO> artists;
	private List<GenreResponseDTO> genres;
	
	public SongResponseDTO(Song song) {
		this.id = song.getId();
		this.name = song.getName();
		this.reproductions = song.getReproductions();
		this.duration = song.getDuration();
		this.album = song.getAlbum() != null ? song.getAlbum().getName() : "";
		this.artists = song.getArtists().stream().map( ArtistResponseDTO::new ).toList();
		this.genres = song.getGenres().stream().map( GenreResponseDTO::new ).toList();
	}
}

package com.music.backend.dto;

import java.util.List;

import com.music.backend.model.Album;
import com.music.backend.model.Artist;
import com.music.backend.model.Genre;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SongRequestDTO {
	@NotNull(message = "name shouldn't be null")
	@NotBlank(message = "name shouldn't be empty")
	private String name;
	
	private Album album;
	
	@NotNull(message = "the song should have at least one artist")
	@NotEmpty(message = "the song should have at least one artist")
	private List<Artist> artists;
	
	@NotNull(message = "the song should have at least one genre")
	@NotEmpty(message = "the song should have at least one genre")
	private List<Genre> genres;
}

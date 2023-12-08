package com.music.backend.dto;

import java.util.List;

import com.music.backend.model.Artist;
import com.music.backend.model.Song;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AlbumRequestDTO {
	@NotNull(message = "name shouldn't be null")
	@NotBlank(message = "name shouldn't be empty")
	private String name;
	
	@NotNull(message = "album should have at least one artist")
	@NotEmpty(message = "album should have at least one artist")
	private List<Artist> artists;
	
	@NotNull(message = "album should have at least one song")
	@NotEmpty(message = "album should have at least one song")
	private List<Song> songs;
}

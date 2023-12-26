package com.music.musicMS.dto;

import java.util.List;

import com.music.musicMS.model.Song;

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
	private List<Integer> artists;
	
	@NotNull(message = "songs should have at least one song")
	@NotEmpty(message = "songs should have at least one song")
	private List<Song> songs;
}

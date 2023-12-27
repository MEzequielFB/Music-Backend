package com.music.musicMS.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlbumUpdateDTO {
	
	@NotNull(message = "name shouldn't be null")
	@NotBlank(message = "name shouldn't be empty")
	private String name;
	
//	@NotNull(message = "songs should have at least one song")
//	private List<Integer> songs;
}

package com.music.musicMS.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GenreRequestDTO {
	@NotNull(message = "name shouldn't be null")
	@NotBlank(message = "name shouldn't be empty")
	private String name;
}

package com.music.musicMS.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlaylistRequestDTO {
	@NotNull(message = "name should't be null")
	@NotBlank(message = "name should't be empty")
	private String name;
	
	@NotNull(message = "isPublic should't be null")
	private boolean isPublic;
	
	@NotNull(message = "user shouldn't be null")
	private Integer user;
}

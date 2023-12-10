package com.music.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlaylistUpdateDTO {
	@NotNull(message = "name should't be null")
	@NotBlank(message = "name should't be empty")
	private String name;
	
	@NotNull(message = "isPublic should't be null")
	private boolean isPublic;
}

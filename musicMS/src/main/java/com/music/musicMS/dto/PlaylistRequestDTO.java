package com.music.musicMS.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistRequestDTO {
	@NotNull(message = "name should't be null")
	@NotBlank(message = "name should't be empty")
	private String name;
	
	@NotNull(message = "isPublic should't be null")
	private Boolean isPublic;
}

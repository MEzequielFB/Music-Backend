package com.music.musicMS.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistUpdateDTO {
	@NotNull(message = "name should't be null")
	@NotBlank(message = "name should't be empty")
	@Schema(name = "name", example = "playlist1")
	private String name;
	
	@NotNull(message = "isPublic should't be null")
	@Schema(name = "isPublic", example = "true")
	private Boolean isPublic;
}

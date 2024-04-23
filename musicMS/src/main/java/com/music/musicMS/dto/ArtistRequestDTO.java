package com.music.musicMS.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistRequestDTO {
	@NotNull(message = "name shouldn't be null")
	@NotBlank(message = "name shouldn't be empty")
	@Schema(name = "name", example = "artist1")
	private String name;
	
	@NotNull(message = "userId shouldn't be null")
	@Schema(name = "userId", example = "1")
	private Integer userId;
}

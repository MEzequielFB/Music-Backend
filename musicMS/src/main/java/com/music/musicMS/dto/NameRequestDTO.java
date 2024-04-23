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
public class NameRequestDTO {

	@NotNull(message = "name shouldn't be null")
	@NotBlank(message = "name shouldn't be blank")
	@Schema(name = "name", example = "artist1")
	private String name;
}

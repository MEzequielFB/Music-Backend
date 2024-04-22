package com.music.musicMS.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongIdDTO {
	@NotNull(message = "songId shouldn't be null")
	@Positive(message = "songId should be positive")
	@Schema(name = "songId", example = "1")
	private Integer songId;
}

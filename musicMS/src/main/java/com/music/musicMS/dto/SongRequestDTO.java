package com.music.musicMS.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class SongRequestDTO {
	@NotNull(message = "name shouldn't be null")
	@NotBlank(message = "name shouldn't be empty")
	@Schema(name = "name", example = "song1")
	private String name;
	
	@NotNull(message = "duration shouldn't be null")
	@Positive(message = "duration should be greater than 0")
	@Schema(name = "duration", example = "240")
	private Integer duration;
	
	@Schema(name = "albumId", example = "1", nullable = true)
	private Integer albumId;
	
	@NotNull(message = "the song should have at least one artist")
	@NotEmpty(message = "the song should have at least one artist")
	@Schema(name = "artists", example = "[1,2,3]")
	private List<Integer> artists;
	
	@NotNull(message = "the song should have at least one genre")
	@NotEmpty(message = "the song should have at least one genre")
	@Schema(name = "genres", example = "[1,2]")
	private List<Integer> genres;
}

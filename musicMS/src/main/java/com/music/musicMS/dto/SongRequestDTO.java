package com.music.musicMS.dto;

import java.util.List;

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
	private String name;
	
	@NotNull(message = "duration shouldn't be null")
	@Positive(message = "duration should be greater than 0")
	private Integer duration;
	
	private Integer albumId;
	
	@NotNull(message = "the song should have at least one artist")
	@NotEmpty(message = "the song should have at least one artist")
	private List<Integer> artists;
	
	@NotNull(message = "the song should have at least one genre")
	@NotEmpty(message = "the song should have at least one genre")
	private List<Integer> genres;
}

package com.music.musicMS.dto;

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
	private Integer songId;
}

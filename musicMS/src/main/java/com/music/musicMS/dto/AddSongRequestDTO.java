package com.music.musicMS.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddSongRequestDTO {
	@NotNull(message = "songId should't be null")
	@Positive(message = "songId should be positive")
	private int songId;
}

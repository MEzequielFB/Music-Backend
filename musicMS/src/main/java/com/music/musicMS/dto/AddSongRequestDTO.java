package com.music.musicMS.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddSongRequestDTO {
	@NotNull(message = "songId should't be null")
	@Positive(message = "songId should be positive")
	private int songId;
}

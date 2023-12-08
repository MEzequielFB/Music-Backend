package com.music.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddSongRequestDTO {
	@NotNull(message = "songId should't be null")
	@PositiveOrZero(message = "songId should be positive or zero")
	private int songId;
}

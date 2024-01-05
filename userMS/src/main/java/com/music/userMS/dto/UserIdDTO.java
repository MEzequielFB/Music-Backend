package com.music.userMS.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserIdDTO {

	@NotNull(message = "userIdd shouldn't be null")
	@Positive(message = "userIdd should be positive")
	private Integer userId;
}

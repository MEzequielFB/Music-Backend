package com.music.userMS.dto;

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
public class UserIdDTO {

	@NotNull(message = "userIdd shouldn't be null")
	@Positive(message = "userIdd should be positive")
	@Schema(name = "userId", example = "1")
	private Integer userId;
}

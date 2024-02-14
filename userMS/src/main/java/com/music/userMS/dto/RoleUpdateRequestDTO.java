package com.music.userMS.dto;

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
public class RoleUpdateRequestDTO {

	@NotNull(message = "role shouldn't be null")
	@NotBlank(message = "role shouldn't be blank")
	private String role;
}

package com.music.merchandisingMS.dto;

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
public class StatusRequestDTO {

	@NotNull(message = "name shouldn't be null")
	@NotBlank(message = "name shouldn't be blank")
	private String name;
}

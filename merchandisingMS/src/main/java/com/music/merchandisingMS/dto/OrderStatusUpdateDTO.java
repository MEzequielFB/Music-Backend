package com.music.merchandisingMS.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class OrderStatusUpdateDTO {

	@NotNull(message = "status shouldn't be null")
	@NotBlank(message = "status shouldn't be blank")
	@Schema(name = "status", example = "status1")
	private String status;
}

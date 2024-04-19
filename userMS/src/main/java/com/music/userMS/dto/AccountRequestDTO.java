package com.music.userMS.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequestDTO {
	
	@NotNull(message = "balance shouldn't be null")
	@PositiveOrZero(message = "balance should be positive or zero")
	@Schema(name = "balance", example = "1500")
	private Double balance;
}

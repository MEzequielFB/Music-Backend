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
public class BalanceDTO {

	@NotNull(message = "balance shouldn't be null")
	@PositiveOrZero(message = "balance should be zero or greater")
	@Schema(name = "balance", example = "1500")
	private Double balance;
}

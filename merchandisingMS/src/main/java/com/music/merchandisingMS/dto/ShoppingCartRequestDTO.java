package com.music.merchandisingMS.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartRequestDTO {
	
	@NotNull(message = "products shouldn't be null")
	@NotEmpty(message = "products shouldn't be empty")
	@Schema(name = "products", example = "[1,1,2]")
	private List<Integer> products;
}

package com.music.merchandisingMS.dto;

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
public class ProductIdDTO {

	@NotNull(message = "productId shouldn't be null")
	@Positive(message = "productId should be positive")
	private Integer productId;
}
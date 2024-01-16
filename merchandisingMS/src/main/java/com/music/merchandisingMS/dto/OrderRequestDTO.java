package com.music.merchandisingMS.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class OrderRequestDTO {
	
	@NotNull(message = "userId shouldn't be null")
	@Positive(message = "userId should be positive")
	private Integer userId;
	
	@NotNull(message = "shippingAddress shouldn't be null")
	@NotBlank(message = "shippingAddress shouldn't be blank")
	private String shippingAddress;
	
	@NotNull(message = "products shouldn't be null")
	@NotEmpty(message = "products shouldn't be empty")
	private List<Integer> products;
}

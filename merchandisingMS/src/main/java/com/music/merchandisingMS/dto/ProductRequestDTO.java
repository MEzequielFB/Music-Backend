package com.music.merchandisingMS.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

	@NotNull(message = "name shouldn't be null")
	@NotBlank(message = "name shouldn't be blank")
	private String name;
	
	@NotNull(message = "price shouldn't be null")
	@Positive(message = "price should be positive")
	private Double price;
	
	@NotNull(message = "stock shouldn't be null")
	@PositiveOrZero(message = "stock should be positive or zero")
	private Integer stock;
	
	@NotNull(message = "tags shouldn't be null")
	@NotEmpty(message = "tags should't be empty")
	private List<Integer> tags;
}

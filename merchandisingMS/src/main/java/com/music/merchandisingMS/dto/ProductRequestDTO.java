package com.music.merchandisingMS.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
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
	@Schema(name = "name", example = "product1")
	private String name;
	
	@NotNull(message = "price shouldn't be null")
	@Positive(message = "price should be positive")
	@Schema(name = "price", example = "49.99")
	private Double price;
	
	@NotNull(message = "stock shouldn't be null")
	@PositiveOrZero(message = "stock should be positive or zero")
	@Schema(name = "stock", example = "10")
	private Integer stock;
	
	@NotNull(message = "tags shouldn't be null")
	@NotEmpty(message = "tags should't be empty")
	@Schema(name = "tags", example = "[1,2]")
	private List<Integer> tags;
}

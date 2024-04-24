package com.music.merchandisingMS.dto;

import java.util.List;

import com.music.merchandisingMS.model.Product;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
	@Schema(name = "id", example = "1")
	private Integer id;
	
	@Schema(name = "name", example = "product1")
	private String name;
	
	@Schema(name = "price", example = "49.99")
	private Double price;
	
	@Schema(name = "stock", example = "10")
	private Integer stock;
	
	@ArraySchema(schema = @Schema(implementation = TagResponseDTO.class))
	private List<TagResponseDTO> tags;
	
	public ProductResponseDTO(Product product) {
		this.id = product.getId();
		this.name = product.getName();
		this.price = product.getPrice();
		this.stock = product.getStock();
		this.tags = product.getTags().stream().map( TagResponseDTO::new ).toList();
	}
}

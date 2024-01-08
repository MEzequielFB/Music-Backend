package com.music.merchandisingMS.dto;

import com.music.merchandisingMS.model.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
	private Integer id;
	private String name;
	private Double price;
	
	public ProductResponseDTO(Product product) {
		this.id = product.getId();
		this.name = product.getName();
		this.price = product.getPrice();
	}
}

package com.music.merchandisingMS.dto;

import java.util.List;

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
	private Integer stock;
	private List<TagResponseDTO> tags;
	
	public ProductResponseDTO(Product product) {
		this.id = product.getId();
		this.name = product.getName();
		this.price = product.getPrice();
		this.stock = product.getStock();
		this.tags = product.getTags().stream().map( TagResponseDTO::new ).toList();
	}
}

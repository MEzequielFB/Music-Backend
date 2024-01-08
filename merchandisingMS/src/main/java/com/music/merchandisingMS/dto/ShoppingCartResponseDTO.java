package com.music.merchandisingMS.dto;

import java.util.List;

import com.music.merchandisingMS.model.ShoppingCart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartResponseDTO {
	private Integer id;
	private Double totalPrice;
	private List<ProductResponseDTO> products;
	
	public ShoppingCartResponseDTO(ShoppingCart shoppingCart) {
		this.id = shoppingCart.getId();
		this.totalPrice = shoppingCart.getTotalPrice();
		this.products = shoppingCart.getProducts().stream().map( ProductResponseDTO::new ).toList();
	}
}

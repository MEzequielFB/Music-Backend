package com.music.merchandisingMS.dto;

import java.util.List;

import com.music.merchandisingMS.model.ShoppingCart;

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
public class ShoppingCartResponseDTO {
	@Schema(name = "id", example = "1")
	private Integer id;
	
	@Schema(name = "totalPrice", example = "49.99")
	private Double totalPrice;
	
	@Schema(implementation = UserDTO.class)
	private UserDTO user;
	
	@ArraySchema(schema = @Schema(implementation = ProductResponseDTO.class))
	private List<ProductResponseDTO> products;
	
	public ShoppingCartResponseDTO(ShoppingCart shoppingCart, UserDTO user) {
		this.id = shoppingCart.getId();
		this.totalPrice = shoppingCart.getTotalPrice();
		this.user = user;
		this.products = shoppingCart.getProducts().stream().map( ProductResponseDTO::new ).toList();
	}
}

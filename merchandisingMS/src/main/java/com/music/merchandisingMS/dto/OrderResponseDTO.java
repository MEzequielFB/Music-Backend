package com.music.merchandisingMS.dto;

import java.util.Date;
import java.util.List;

import com.music.merchandisingMS.model.Order;

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
public class OrderResponseDTO {
	@Schema(name = "id", example = "1")
	private Integer id;
	
	@Schema(implementation = UserDTO.class)
	private UserDTO user;
	
	@Schema(name = "createdAt", example = "2024-01-01 12:00:00")
	private Date createdAt;
	
	@Schema(name = "deliveredDate", example = "2024-01-08 16:30:00")
	private Date deliveredDate;
	
	@Schema(name = "totalPrice", example = "49.99")
	private Double totalPrice;
	
	@Schema(implementation = StatusResponseDTO.class)
	private StatusResponseDTO status;
	
	@ArraySchema(schema = @Schema(implementation = ProductResponseDTO.class))
	private List<ProductResponseDTO> products;
	
	public OrderResponseDTO(Order order, UserDTO user) {
		this.id = order.getId();
		this.user = user;
		this.createdAt = order.getCreatedAt();
		this.deliveredDate = order.getDeliveredDate();
		this.totalPrice = order.getTotalPrice();
		this.status = new StatusResponseDTO(order.getStatus());
		this.products = order.getProducts().stream().map( ProductResponseDTO::new ).toList();
	}
}

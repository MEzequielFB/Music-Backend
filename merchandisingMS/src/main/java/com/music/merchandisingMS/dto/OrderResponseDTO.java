package com.music.merchandisingMS.dto;

import java.util.Date;
import java.util.List;

import com.music.merchandisingMS.model.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
	private Integer id;
	private UserDTO user;
	private Date createdAt;
	private Date deliveredDate;
	private Double totalPrice;
	private StatusResponseDTO status;
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

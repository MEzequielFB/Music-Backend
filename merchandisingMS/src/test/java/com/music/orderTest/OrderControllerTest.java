package com.music.orderTest;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.music.merchandisingMS.controller.OrderController;
import com.music.merchandisingMS.dto.OrderRequestDTO;
import com.music.merchandisingMS.dto.OrderResponseDTO;
import com.music.merchandisingMS.dto.ProductResponseDTO;
import com.music.merchandisingMS.dto.StatusResponseDTO;
import com.music.merchandisingMS.dto.TagResponseDTO;
import com.music.merchandisingMS.dto.UserDTO;
import com.music.merchandisingMS.service.OrderService;

public class OrderControllerTest {

	@Mock
	private OrderService service;
	
	@InjectMocks
	private OrderController controller;
	
	private OrderResponseDTO orderResponseMock;
	private OrderRequestDTO orderRequestMock;
	
	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
		this.orderResponseMock = OrderResponseDTO.builder()
				.id(1)
				.user(UserDTO.builder()
						.id(1)
						.username("eze")
						.email("eze@gmail.com")
						.address("street 123")
						.role("USER")
						.build())
				.createdAt(new Date(System.currentTimeMillis()))
				.deliveredDate(null)
				.totalPrice(300.0)
				.status(StatusResponseDTO.builder()
						.id(1)
						.name("PENDING")
						.build())
				.products(List.of(ProductResponseDTO.builder()
						.id(1)
						.name("Note black shirt")
						.price(20.0)
						.stock(5)
						.tags(List.of(TagResponseDTO.builder()
								.id(1)
								.name("t-shirt")
								.build()))
						.build()))
				.build();
		this.orderRequestMock = OrderRequestDTO.builder()
				.userId(1)
				.shippingAddress("street 123")
				.products(List.of(1))
				.build();
	}
}

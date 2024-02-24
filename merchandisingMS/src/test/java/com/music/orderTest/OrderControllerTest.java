package com.music.orderTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.music.merchandisingMS.controller.OrderController;
import com.music.merchandisingMS.dto.OrderRequestDTO;
import com.music.merchandisingMS.dto.OrderResponseDTO;
import com.music.merchandisingMS.dto.OrderStatusUpdateDTO;
import com.music.merchandisingMS.dto.ProductResponseDTO;
import com.music.merchandisingMS.dto.StatusResponseDTO;
import com.music.merchandisingMS.dto.TagResponseDTO;
import com.music.merchandisingMS.dto.UserDTO;
import com.music.merchandisingMS.exception.NotFoundException;
import com.music.merchandisingMS.service.OrderService;

public class OrderControllerTest {

	@Mock
	private OrderService service;
	
	@InjectMocks
	private OrderController controller;
	
	private OrderResponseDTO orderResponseMock;
	private OrderStatusUpdateDTO orderStatusUpdateMock;
	private String tokenMock;
	
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
		this.orderStatusUpdateMock = OrderStatusUpdateDTO.builder()
				.status("SHIPPED")
				.build();
		this.tokenMock = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdXBlcmFkbWluQGdtYWlsLmNvbSIsImlkIjoyMywiYXV0aCI6IlNVUEVSX0FETUlOIiwiZXhwIjoxNzA3NTMyNDU1fQ.JWKKgNu9xZs6c0lr7ZeRd2v_FFCyTle7XpbumUMeQXPLZYJ1TfTydpESzImAnbljMgZ-OrKCVTHytbVyl4edbQ";
	}
	
	@Test
	public void findAllTest() {
		List<OrderResponseDTO> ordersResponseMock = List.of(orderResponseMock);
		when(service.findAll(tokenMock)).thenReturn(ordersResponseMock);
		
		ResponseEntity<List<OrderResponseDTO>> responseEntity = controller.findAll(tokenMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(ordersResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void findByIdTest() throws NotFoundException {
		when(service.findById(1, tokenMock)).thenReturn(orderResponseMock);
		
		ResponseEntity<OrderResponseDTO> responseEntity = controller.findById(1, tokenMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(orderResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void updateOrderStatusTest() throws NotFoundException {
		orderResponseMock.getStatus().setName("SHIPPED");
		when(service.updateOrderStatus(1, orderStatusUpdateMock, tokenMock)).thenReturn(orderResponseMock);
		
		ResponseEntity<OrderResponseDTO> responseEntity = controller.updateOrderStatus(1, orderStatusUpdateMock, tokenMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(orderResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void deleteOrderTest() throws NotFoundException {
		when(service.deleteOrder(1, tokenMock)).thenReturn(orderResponseMock);
		
		ResponseEntity<OrderResponseDTO> responseEntity = controller.deleteOrder(1, tokenMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(orderResponseMock, responseEntity.getBody());
	}
}

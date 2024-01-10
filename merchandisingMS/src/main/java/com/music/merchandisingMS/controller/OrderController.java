package com.music.merchandisingMS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.music.merchandisingMS.dto.OrderRequestDTO;
import com.music.merchandisingMS.dto.OrderResponseDTO;
import com.music.merchandisingMS.dto.OrderStatusUpdateDTO;
import com.music.merchandisingMS.exception.NotFoundException;
import com.music.merchandisingMS.exception.SomeEntityDoesNotExistException;
import com.music.merchandisingMS.service.OrderService;

import jakarta.validation.Valid;

@RestController("orderController")
@RequestMapping("/api/order")
public class OrderController {

	@Autowired
	private OrderService service;
	
	@GetMapping("")
	public ResponseEntity<List<OrderResponseDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<OrderResponseDTO> findById(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@PostMapping("")
	public ResponseEntity<OrderResponseDTO> saveOrder(@RequestBody @Valid OrderRequestDTO request) throws SomeEntityDoesNotExistException, NotFoundException {
		return new ResponseEntity<>(service.saveOrder(request), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable Integer id, @RequestBody @Valid OrderStatusUpdateDTO request) throws NotFoundException {
		return ResponseEntity.ok(service.updateOrderStatus(id, request));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<OrderResponseDTO> deleteOrder(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.deleteOrder(id));
	}
}

package com.music.merchandisingMS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.music.merchandisingMS.dto.OrderResponseDTO;
import com.music.merchandisingMS.dto.OrderStatusUpdateDTO;
import com.music.merchandisingMS.exception.NotFoundException;
import com.music.merchandisingMS.model.Roles;
import com.music.merchandisingMS.service.OrderService;

import jakarta.validation.Valid;

@RestController("orderController")
@RequestMapping("/api/order")
public class OrderController { // ONLY ADMINS AND DELIVERIES

	@Autowired
	private OrderService service;
	
	@GetMapping("")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.DELIVERY + "')")
	public ResponseEntity<List<OrderResponseDTO>> findAll(@RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findAll(token));
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.DELIVERY + "')")
	public ResponseEntity<OrderResponseDTO> findById(@PathVariable Integer id, @RequestHeader("Authorization") String token) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id, token));
	}
	
//	@PostMapping("")
//	public ResponseEntity<OrderResponseDTO> saveOrder(@RequestBody @Valid OrderRequestDTO request) throws SomeEntityDoesNotExistException, NotFoundException {
//		return new ResponseEntity<>(service.saveOrder(request), HttpStatus.CREATED);
//	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.DELIVERY + "')")
	public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable Integer id, @RequestBody @Valid OrderStatusUpdateDTO request, @RequestHeader("Authorization") String token) throws NotFoundException {
		return ResponseEntity.ok(service.updateOrderStatus(id, request, token));
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.DELIVERY + "')")
	public ResponseEntity<OrderResponseDTO> deleteOrder(@PathVariable Integer id, @RequestHeader("Authorization") String token) throws NotFoundException {
		return ResponseEntity.ok(service.deleteOrder(id, token));
	}
}

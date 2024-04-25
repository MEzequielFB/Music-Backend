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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController("orderController")
@RequestMapping("/api/order")
@Tag(name = "Order Controller", description = "<p>Used for order management: return/delete orders, update orders status</p>")
public class OrderController { // ONLY ADMINS AND DELIVERIES

	@Autowired
	private OrderService service;
	
	@Operation(summary = "Find all orders", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>DELIVERY</li></ul> ",
			parameters = {
				@Parameter(name = "Authorization", description = "Authentication token provided when loggin in or registering", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Orders found", content = {
			@Content(array = @ArraySchema(schema = @Schema(implementation = OrderResponseDTO.class)))
		}),
		@ApiResponse(responseCode = "403", description = "Role authorization exception", content = {
			@Content(schema = @Schema(example = "The current user is not authorized to perform action"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.DELIVERY + "')")
	public ResponseEntity<List<OrderResponseDTO>> findAll(@RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findAll(token));
	}
	
	@Operation(summary = "Find order by id", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>DELIVERY</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Order id", required = true),
				@Parameter(name = "Authorization", description = "Authentication token provided when loggin in or registering", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Order found", content = {
			@Content(schema = @Schema(implementation = OrderResponseDTO.class))
		}),
		@ApiResponse(responseCode = "403", description = "Role authorization exception", content = {
			@Content(schema = @Schema(example = "The current user is not authorized to perform action"))
		}),
		@ApiResponse(responseCode = "404", description = "Order/User not found", content = {
			@Content(schema = @Schema(example = "The entity Order/User with id '1'/'1' doesn't exist"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.DELIVERY + "')")
	public ResponseEntity<OrderResponseDTO> findById(@PathVariable Integer id, @RequestHeader("Authorization") String token) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id, token));
	}
	
//	@PostMapping("")
//	public ResponseEntity<OrderResponseDTO> saveOrder(@RequestBody @Valid OrderRequestDTO request) throws SomeEntityDoesNotExistException, NotFoundException {
//		return new ResponseEntity<>(service.saveOrder(request), HttpStatus.CREATED);
//	}
	
	@Operation(summary = "Update order status", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>DELIVERY</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Order id", required = true),
				@Parameter(name = "Authorization", description = "Authentication token provided when loggin in or registering", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Order status updated", content = {
			@Content(schema = @Schema(implementation = OrderResponseDTO.class))
		}),
		@ApiResponse(responseCode = "403", description = "Role authorization exception", content = {
			@Content(schema = @Schema(example = "The current user is not authorized to perform action"))
		}),
		@ApiResponse(responseCode = "404", description = "Order/User/Status not found", content = {
			@Content(schema = @Schema(example = "The entity Order/User/Status with id '1'/'1'/'1' doesn't exist"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.DELIVERY + "')")
	public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable Integer id, @RequestBody @Valid OrderStatusUpdateDTO request, @RequestHeader("Authorization") String token) throws NotFoundException {
		return ResponseEntity.ok(service.updateOrderStatus(id, request, token));
	}
	
	@Operation(summary = "Delete order", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>DELIVERY</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Order id", required = true),
				@Parameter(name = "Authorization", description = "Authentication token provided when loggin in or registering", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Order deleted", content = {
			@Content(schema = @Schema(implementation = OrderResponseDTO.class))
		}),
		@ApiResponse(responseCode = "403", description = "Role authorization exception", content = {
			@Content(schema = @Schema(example = "The current user is not authorized to perform action"))
		}),
		@ApiResponse(responseCode = "404", description = "Order/User not found", content = {
			@Content(schema = @Schema(example = "The entity Order/User with id '1'/'1' doesn't exist"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.DELIVERY + "')")
	public ResponseEntity<OrderResponseDTO> deleteOrder(@PathVariable Integer id, @RequestHeader("Authorization") String token) throws NotFoundException {
		return ResponseEntity.ok(service.deleteOrder(id, token));
	}
}

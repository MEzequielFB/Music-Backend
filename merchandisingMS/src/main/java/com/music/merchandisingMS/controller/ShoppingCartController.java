package com.music.merchandisingMS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.music.merchandisingMS.dto.ProductIdDTO;
import com.music.merchandisingMS.dto.ProductQuantityRequestDTO;
import com.music.merchandisingMS.dto.ShoppingCartRequestDTO;
import com.music.merchandisingMS.dto.ShoppingCartResponseDTO;
import com.music.merchandisingMS.exception.AccountIdRequest;
import com.music.merchandisingMS.exception.AuthorizationException;
import com.music.merchandisingMS.exception.DeletedEntityException;
import com.music.merchandisingMS.exception.EmptyShoppingCartException;
import com.music.merchandisingMS.exception.EntityWithUserIdAlreadyUsedException;
import com.music.merchandisingMS.exception.NotFoundException;
import com.music.merchandisingMS.exception.SomeEntityDoesNotExistException;
import com.music.merchandisingMS.exception.StockException;
import com.music.merchandisingMS.model.Roles;
import com.music.merchandisingMS.service.ShoppingCartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController("shoppingCartController")
@RequestMapping("/api/shoppingCart")
public class ShoppingCartController {

	@Autowired
	private ShoppingCartService service;
	
	@Operation(summary = "Find all shopping carts", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')")
	public ResponseEntity<List<ShoppingCartResponseDTO>> findAll(@RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findAll(token));
	}
	
	@Operation(summary = "Find shopping cart by id", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')")
	public ResponseEntity<ShoppingCartResponseDTO> findById(@PathVariable Integer id, @RequestHeader("Authorization") String token) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id, token));
	}
	
	@Operation(summary = "Find shopping cart by logged user", description = "<p>Required roles:</p> <ul><li>USER</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/loggedUser")
	@PreAuthorize("hasAuthority('" + Roles.USER + "')")
	public ResponseEntity<ShoppingCartResponseDTO> findByLoggedUser(@RequestHeader("Authorization") String token) throws AuthorizationException, NotFoundException {
		return ResponseEntity.ok(service.findByLoggedUser(token));
	}
	
	@Operation(summary = "Save shopping cart. Cannot be two shopping carts referencing the same user, the products should exists or not be deleted and the products should have stock. The total price is calculated taking the products's price and discount", description = "<p>Required roles:</p> <ul><li>USER</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@PostMapping("")
	@PreAuthorize("hasAuthority('" + Roles.USER + "')")
	public ResponseEntity<ShoppingCartResponseDTO> saveShoppingCart(@RequestBody @Valid ShoppingCartRequestDTO request, @RequestHeader("Authorization") String token) throws EntityWithUserIdAlreadyUsedException, SomeEntityDoesNotExistException, NotFoundException, StockException, DeletedEntityException, AuthorizationException {
		return new ResponseEntity<>(service.saveShoppingCart(request, token), HttpStatus.CREATED);
	}
	
	@Operation(summary = "Buy products from shopping cart. The shopping cart shouldn't be empty, the products should have stock, the user's account should have enough balance to afford the products. If the products can be afford, it's stock will be reduced. When the purchase is done, an order with status PENDING will be created", description = "<p>Required roles:</p> <ul><li>USER</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("/{id}/buy")
	@PreAuthorize("hasAuthority('" + Roles.USER + "')")
	public ResponseEntity<ShoppingCartResponseDTO> buyProducts(@PathVariable Integer id, @RequestBody @Valid AccountIdRequest request, @RequestHeader("Authorization") String token) throws NotFoundException, EmptyShoppingCartException, StockException, AuthorizationException {
		return ResponseEntity.ok(service.buyProducts(id, request.getAccountId(), token));
	}

	@Operation(summary = "Add product to shopping cart. The product should have stock and the logged user should be the owner of the shopping cart", description = "<p>Required roles:</p> <ul><li>USER</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("/{id}/addProduct")
	@PreAuthorize("hasAuthority('" + Roles.USER + "')")
	public ResponseEntity<ShoppingCartResponseDTO> addProduct(@PathVariable Integer id, @RequestBody @Valid ProductQuantityRequestDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, DeletedEntityException, StockException, AuthorizationException {
		return ResponseEntity.ok(service.addProduct(id, request, token));
	}

	@Operation(summary = "Remove products from shopping cart. The logged user should be the owner of the shopping cart. All the samples of the selected product will be removed", description = "<p>Required roles:</p> <ul><li>USER</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("/{id}/removeProduct")
	@PreAuthorize("hasAuthority('" + Roles.USER + "')")
	public ResponseEntity<ShoppingCartResponseDTO> removeProduct(@PathVariable Integer id, @RequestBody @Valid ProductIdDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, AuthorizationException {
		return ResponseEntity.ok(service.removeProduct(id, request.getProductId(), token));
	}
	
	@Operation(summary = "Delete shopping cart", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')")
	public ResponseEntity<ShoppingCartResponseDTO> deleteShoppingCart(@PathVariable Integer id, @RequestHeader("Authorization") String token) throws NotFoundException {
		return ResponseEntity.ok(service.deleteShoppingCart(id, token));
	}
}

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
import com.music.merchandisingMS.exception.DeletedEntityException;
import com.music.merchandisingMS.exception.EmptyShoppingCartException;
import com.music.merchandisingMS.exception.EntityWithUserIdAlreadyUsedException;
import com.music.merchandisingMS.exception.NotFoundException;
import com.music.merchandisingMS.exception.SomeEntityDoesNotExistException;
import com.music.merchandisingMS.exception.StockException;
import com.music.merchandisingMS.model.Roles;
import com.music.merchandisingMS.service.ShoppingCartService;

import jakarta.validation.Valid;

@RestController("shoppingCartController")
@RequestMapping("/api/shoppingCart")
public class ShoppingCartController {

	@Autowired
	private ShoppingCartService service;
	
	@GetMapping("")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')")
	public ResponseEntity<List<ShoppingCartResponseDTO>> findAll(@RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findAll(token));
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')")
	public ResponseEntity<ShoppingCartResponseDTO> findById(@PathVariable Integer id, @RequestHeader("Authorization") String token) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id, token));
	}
	
	@PostMapping("")
	@PreAuthorize("hasAuthority('" + Roles.USER + "')")
	public ResponseEntity<ShoppingCartResponseDTO> saveShoppingCart(@RequestBody @Valid ShoppingCartRequestDTO request, @RequestHeader("Authorization") String token) throws EntityWithUserIdAlreadyUsedException, SomeEntityDoesNotExistException, NotFoundException, StockException, DeletedEntityException {
		return new ResponseEntity<>(service.saveShoppingCart(request, token), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}/buy")
	@PreAuthorize("hasAuthority('" + Roles.USER + "')")
	public ResponseEntity<ShoppingCartResponseDTO> buyProducts(@PathVariable Integer id, @RequestBody @Valid AccountIdRequest request, @RequestHeader("Authorization") String token) throws NotFoundException, EmptyShoppingCartException, StockException {
		return ResponseEntity.ok(service.buyProducts(id, request.getAccountId(), token));
	}
	
	@PutMapping("/{id}/addProduct")
	@PreAuthorize("hasAuthority('" + Roles.USER + "')")
	public ResponseEntity<ShoppingCartResponseDTO> addProduct(@PathVariable Integer id, @RequestBody @Valid ProductQuantityRequestDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, DeletedEntityException, StockException {
		return ResponseEntity.ok(service.addProduct(id, request, token));
	}
	
	@PutMapping("/{id}/removeProduct")
	@PreAuthorize("hasAuthority('" + Roles.USER + "')")
	public ResponseEntity<ShoppingCartResponseDTO> removeProduct(@PathVariable Integer id, @RequestBody @Valid ProductIdDTO request, @RequestHeader("Authorization") String token) throws NotFoundException {
		return ResponseEntity.ok(service.removeProduct(id, request.getProductId(), token));
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')")
	public ResponseEntity<ShoppingCartResponseDTO> deleteShoppingCart(@PathVariable Integer id, @RequestHeader("Authorization") String token) throws NotFoundException {
		return ResponseEntity.ok(service.deleteShoppingCart(id, token));
	}
}

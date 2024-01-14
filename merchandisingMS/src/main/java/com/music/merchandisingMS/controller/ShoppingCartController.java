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
import com.music.merchandisingMS.service.ShoppingCartService;

import jakarta.validation.Valid;

@RestController("shoppingCartController")
@RequestMapping("/api/shoppingCart")
public class ShoppingCartController {

	@Autowired
	private ShoppingCartService service;
	
	@GetMapping("")
	public ResponseEntity<List<ShoppingCartResponseDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ShoppingCartResponseDTO> findById(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@PostMapping("")
	public ResponseEntity<ShoppingCartResponseDTO> saveShoppingCart(@RequestBody @Valid ShoppingCartRequestDTO request) throws EntityWithUserIdAlreadyUsedException, SomeEntityDoesNotExistException, NotFoundException, StockException, DeletedEntityException {
		return new ResponseEntity<>(service.saveShoppingCart(request), HttpStatus.CREATED);
	}
	
//	@PutMapping("/{id}/buy")
//	public ResponseEntity<ShoppingCartResponseDTO> buyProducts(@PathVariable Integer id, @RequestBody @Valid AccountIdRequest request) throws NotFoundException, EmptyShoppingCartException, StockException {
//		return ResponseEntity.ok(service.buyProducts(id, request.getAccountId()));
//	}
	
	@PutMapping("/{id}/addProduct")
	public ResponseEntity<ShoppingCartResponseDTO> addProduct(@PathVariable Integer id, @RequestBody @Valid ProductQuantityRequestDTO request) throws NotFoundException, DeletedEntityException, StockException {
		return ResponseEntity.ok(service.addProduct(id, request));
	}
	
	@PutMapping("/{id}/removeProduct")
	public ResponseEntity<ShoppingCartResponseDTO> removeProduct(@PathVariable Integer id, @RequestBody @Valid ProductIdDTO request) throws NotFoundException {
		return ResponseEntity.ok(service.removeProduct(id, request.getProductId()));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ShoppingCartResponseDTO> deleteShoppingCart(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.deleteShoppingCart(id));
	}
}

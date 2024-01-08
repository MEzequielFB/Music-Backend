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

import com.music.merchandisingMS.dto.ProductRequestDTO;
import com.music.merchandisingMS.dto.ProductResponseDTO;
import com.music.merchandisingMS.exception.NameAlreadyUsedException;
import com.music.merchandisingMS.exception.NotFoundException;
import com.music.merchandisingMS.exception.SomeEntityDoesNotExistException;
import com.music.merchandisingMS.service.ProductService;

import jakarta.validation.Valid;

@RestController("productController")
@RequestMapping("/api/product")
public class ProductController {

	@Autowired
	private ProductService service;
	
	@GetMapping("")
	public ResponseEntity<List<ProductResponseDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ProductResponseDTO> findById(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@PostMapping("")
	public ResponseEntity<ProductResponseDTO> saveProduct(@RequestBody @Valid ProductRequestDTO request) throws NameAlreadyUsedException, SomeEntityDoesNotExistException {
		return new ResponseEntity<>(service.saveProduct(request), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Integer id, @RequestBody @Valid ProductRequestDTO request) throws NotFoundException, SomeEntityDoesNotExistException {
		return ResponseEntity.ok(service.updateProduct(id, request));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ProductResponseDTO> deleteProduct(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.deleteProduct(id));
	}
}

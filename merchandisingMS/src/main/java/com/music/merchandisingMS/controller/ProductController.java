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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.music.merchandisingMS.dto.ProductRequestDTO;
import com.music.merchandisingMS.dto.ProductResponseDTO;
import com.music.merchandisingMS.exception.DeletedEntityException;
import com.music.merchandisingMS.exception.NameAlreadyUsedException;
import com.music.merchandisingMS.exception.NotFoundException;
import com.music.merchandisingMS.exception.SomeEntityDoesNotExistException;
import com.music.merchandisingMS.model.Roles;
import com.music.merchandisingMS.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController("productController")
@RequestMapping("/api/product")
public class ProductController {

	@Autowired
	private ProductService service;
	
	@Operation(summary = "Find all products", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "')")
	public ResponseEntity<List<ProductResponseDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@Operation(summary = "Find all deleted products", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/deleted")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')")
	public ResponseEntity<List<ProductResponseDTO>> findAllDeletedProducts() {
		return ResponseEntity.ok(service.findAllDeletedProducts());
	}

	@Operation(summary = "Find product by id", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "')")
	public ResponseEntity<ProductResponseDTO> findById(@PathVariable Integer id) throws NotFoundException, DeletedEntityException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@Operation(summary = "Find products by tag", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/tag/{tagName}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "')")
	public ResponseEntity<List<ProductResponseDTO>> findAllByTag(@PathVariable String tagName) throws NotFoundException {
		return ResponseEntity.ok(service.findAllByTag(tagName));
	}
	
	@Operation(summary = "Save product", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@PostMapping("")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')")
	public ResponseEntity<ProductResponseDTO> saveProduct(@RequestBody @Valid ProductRequestDTO request) throws NameAlreadyUsedException, SomeEntityDoesNotExistException {
		return new ResponseEntity<>(service.saveProduct(request), HttpStatus.CREATED);
	}
	
	@Operation(summary = "Update product", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')")
	public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Integer id, @RequestBody @Valid ProductRequestDTO request) throws NotFoundException, SomeEntityDoesNotExistException, DeletedEntityException, NameAlreadyUsedException {
		return ResponseEntity.ok(service.updateProduct(id, request));
	}
	
	@Operation(summary = "Delete product. Is a logic delete because cannot delete referenced products on Order", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')")
	public ResponseEntity<ProductResponseDTO> deleteProduct(@PathVariable Integer id) throws NotFoundException, DeletedEntityException {
		return ResponseEntity.ok(service.deleteProduct(id));
	}
}

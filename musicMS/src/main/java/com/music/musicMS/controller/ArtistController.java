package com.music.musicMS.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.music.musicMS.dto.ArtistRequestDTO;
import com.music.musicMS.dto.ArtistResponseDTO;
import com.music.musicMS.dto.NameRequestDTO;
import com.music.musicMS.exception.AuthorizationException;
import com.music.musicMS.exception.NameAlreadyUsedException;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.model.Roles;
import com.music.musicMS.service.ArtistService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/artist")
public class ArtistController {

	@Autowired
	private ArtistService service;
	
	@Operation(summary = "Find all artists", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<List<ArtistResponseDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@Operation(summary = "Find all artists by name", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/search")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<List<ArtistResponseDTO>> findAllByName(@RequestParam(required = false) String name) {
		return ResponseEntity.ok(service.findAllByName(name));
	}
	
	@Operation(summary = "Find all deleted artists", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/deleted")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')")
	public ResponseEntity<List<ArtistResponseDTO>> findAllDeletedArtists() {
		return ResponseEntity.ok(service.findAllDeleted());
	}
	
	@Operation(summary = "Find artist by id", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<ArtistResponseDTO> findById(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@Operation(summary = "Find artists by ids", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/allByIds")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<List<ArtistResponseDTO>> findAllByIds(@RequestParam List<Integer> ids) {
		return ResponseEntity.ok(service.findAllByids(ids));
	}

	@Operation(summary = "Save an artist", description = "<p>Permit all roles</p>")
	@Hidden
	@PostMapping("") // permit all
	public ResponseEntity<ArtistResponseDTO> saveArtist(@RequestBody @Valid ArtistRequestDTO request) throws NameAlreadyUsedException {
		return new ResponseEntity<>(service.saveArtist(request), HttpStatus.CREATED);
	}
	
	@Operation(summary = "Update an artist", description = "<p>Required roles:</p> <ul><li>ARTIST</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@Hidden
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('" + Roles.ARTIST + "')")
	public ResponseEntity<ArtistResponseDTO> updateArtist(@PathVariable Integer id, @RequestBody @Valid NameRequestDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, AuthorizationException {
		return ResponseEntity.ok(service.updateArtist(id, request, token));
	}
	
	@Operation(summary = "Update an artist by userId", description = "<p>Required roles:</p> <ul><li>ARTIST</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@Hidden
	@PutMapping("/user/{userId}")
	@PreAuthorize("hasAuthority('" + Roles.ARTIST + "')")
	public ResponseEntity<ArtistResponseDTO> updateArtistByUserId(@PathVariable Integer userId, @RequestBody @Valid NameRequestDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, AuthorizationException {
		return ResponseEntity.ok(service.updateArtistByUserId(userId, request, token));
	}
	
	@Operation(summary = "Delete an artist", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@Hidden
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')")
	public ResponseEntity<ArtistResponseDTO> deleteArtist(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.deleteArtist(id));
	}
	
	@Operation(summary = "Delete an artist by userId", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@Hidden
	@DeleteMapping("/user/{userId}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')")
	public ResponseEntity<ArtistResponseDTO> deleteArtistByUserId(@PathVariable Integer userId) throws NotFoundException {
		return ResponseEntity.ok(service.deleteArtistByUserId(userId));
	}
}

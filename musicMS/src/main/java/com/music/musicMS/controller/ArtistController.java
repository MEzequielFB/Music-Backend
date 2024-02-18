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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/artist")
public class ArtistController {

	@Autowired
	private ArtistService service;
	
	@GetMapping("")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<List<ArtistResponseDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<ArtistResponseDTO>> findAllByName(@RequestParam(required = false) String name) {
		return ResponseEntity.ok(service.findAllByName(name));
	}
	
	@GetMapping("/deleted")
	@PreAuthorize("hasAuthority('" + Roles.ADMIN + "')")
	public ResponseEntity<List<ArtistResponseDTO>> findAllDeletedArtists() {
		return ResponseEntity.ok(service.findAllDeleted());
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<ArtistResponseDTO> findById(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@GetMapping("/allByIds")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<List<ArtistResponseDTO>> findAllByIds(@RequestParam List<Integer> ids) {
		return ResponseEntity.ok(service.findAllByids(ids));
	}
	
	@PostMapping("") // permit all
	public ResponseEntity<ArtistResponseDTO> saveArtist(@RequestBody @Valid ArtistRequestDTO request) throws NameAlreadyUsedException {
		return new ResponseEntity<>(service.saveArtist(request), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('" + Roles.ARTIST + "')")
	public ResponseEntity<ArtistResponseDTO> updateArtist(@PathVariable Integer id, @RequestBody @Valid NameRequestDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, AuthorizationException {
		return ResponseEntity.ok(service.updateArtist(id, request, token));
	}
	
	@PutMapping("/user/{userId}")
	@PreAuthorize("hasAuthority('" + Roles.ARTIST + "')")
	public ResponseEntity<ArtistResponseDTO> updateArtistByUserId(@PathVariable Integer userId, @RequestBody @Valid NameRequestDTO request) throws NotFoundException {
		return ResponseEntity.ok(service.updateArtistByUserId(userId, request));
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('" + Roles.ADMIN + "')")
	public ResponseEntity<ArtistResponseDTO> deleteArtist(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.deleteArtist(id));
	}
	
	@DeleteMapping("/user/{userId}")
	@PreAuthorize("hasAuthority('" + Roles.ADMIN + "')")
	public ResponseEntity<ArtistResponseDTO> deleteArtistByUserId(@PathVariable Integer userId) throws NotFoundException {
		return ResponseEntity.ok(service.deleteArtistByUserId(userId));
	}
}

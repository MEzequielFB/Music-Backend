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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.music.musicMS.dto.GenreRequestDTO;
import com.music.musicMS.dto.GenreResponseDTO;
import com.music.musicMS.exception.NameAlreadyUsedException;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.model.Roles;
import com.music.musicMS.service.GenreService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/genre")
public class GenreController {

	@Autowired
	private GenreService service;
	
	@GetMapping("")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<List<GenreResponseDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<GenreResponseDTO> findById(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@PostMapping("")
	@PreAuthorize( "hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')" )
	public ResponseEntity<GenreResponseDTO> saveGenre(@RequestBody @Valid GenreRequestDTO request) throws NameAlreadyUsedException {
		return new ResponseEntity<>(service.saveGenre(request), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	@PreAuthorize( "hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')" )
	public ResponseEntity<GenreResponseDTO> updateGenre(@PathVariable Integer id, @RequestBody @Valid GenreRequestDTO request) throws NotFoundException {
		return ResponseEntity.ok(service.updateGenre(id, request));
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize( "hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')" )
	public ResponseEntity<GenreResponseDTO> deleteGenre(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.deleteGenre(id));
	}
}

package com.music.backend.controller;

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

import com.music.backend.dto.GenreRequestDTO;
import com.music.backend.dto.GenreResponseDTO;
import com.music.backend.exception.NameAlreadyUsedException;
import com.music.backend.exception.NotFoundException;
import com.music.backend.service.GenreService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/genre")
public class GenreController {

	@Autowired
	private GenreService service;
	
	@GetMapping("")
	public ResponseEntity<List<GenreResponseDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<GenreResponseDTO> findById(@PathVariable int id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@PostMapping("")
	public ResponseEntity<GenreResponseDTO> saveGenre(@RequestBody @Valid GenreRequestDTO request) throws NameAlreadyUsedException {
		return new ResponseEntity<>(service.saveGenre(request), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<GenreResponseDTO> updateGenre(@PathVariable int id, @RequestBody @Valid GenreRequestDTO request) throws NotFoundException {
		return ResponseEntity.ok(service.updateGenre(id, request));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<GenreResponseDTO> deleteGenre(@PathVariable int id) throws NotFoundException {
		return ResponseEntity.ok(service.deleteGenre(id));
	}
}

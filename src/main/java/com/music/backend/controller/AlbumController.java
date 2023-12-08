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

import com.music.backend.dto.AlbumRequestDTO;
import com.music.backend.dto.AlbumResponseDTO;
import com.music.backend.exception.NotFoundException;
import com.music.backend.service.AlbumService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/album")
public class AlbumController {

	@Autowired
	private AlbumService service;
	
	@GetMapping("")
	public ResponseEntity<List<AlbumResponseDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<AlbumResponseDTO> findById(@PathVariable int id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@PostMapping("")
	public ResponseEntity<AlbumResponseDTO> saveAlbum(@RequestBody @Valid AlbumRequestDTO request) {
		return new ResponseEntity<>(service.saveAlbum(request), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<AlbumResponseDTO> updateAlbum(@PathVariable int id, @RequestBody @Valid AlbumRequestDTO request) throws NotFoundException {
		return ResponseEntity.ok(service.updateAlbum(id, request));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<AlbumResponseDTO> deleteAlbum(@PathVariable int id) throws NotFoundException {
		return ResponseEntity.ok(service.deleteAlbum(id));
	}
}

package com.music.userMS.controller;

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

import com.music.userMS.dto.ArtistRequestDTO;
import com.music.userMS.dto.ArtistResponseDTO;
import com.music.userMS.exception.BadParamsException;
import com.music.userMS.exception.NotFoundException;
import com.music.userMS.service.ArtistService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/artist")
public class ArtistController {

	@Autowired
	private ArtistService service;
	
	@GetMapping("")
	public ResponseEntity<List<ArtistResponseDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ArtistResponseDTO> findById(@PathVariable int id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@GetMapping("/allByIds")
	public ResponseEntity<List<ArtistResponseDTO>> findAllByIds(@PathVariable List<Integer> request) throws BadParamsException {
		return ResponseEntity.ok(service.findAllByids(request));
	}
	
	@PostMapping("")
	public ResponseEntity<ArtistResponseDTO> saveArtist(@RequestBody @Valid ArtistRequestDTO request) {
		return new ResponseEntity<>(service.saveArtist(request), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ArtistResponseDTO> updateArtist(@PathVariable int id, @RequestBody @Valid ArtistRequestDTO request) throws NotFoundException {
		return ResponseEntity.ok(service.updateArtist(id, request));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ArtistResponseDTO> deleteArtist(@PathVariable int id) throws NotFoundException {
		return ResponseEntity.ok(service.deleteArtist(id));
	}
}

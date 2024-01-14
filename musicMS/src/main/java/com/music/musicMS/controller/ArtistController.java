package com.music.musicMS.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.music.musicMS.dto.ArtistRequestDTO;
import com.music.musicMS.dto.ArtistResponseDTO;
import com.music.musicMS.exception.NameAlreadyUsedException;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.service.ArtistService;

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
	
	@GetMapping("/deleted")
	public ResponseEntity<List<ArtistResponseDTO>> findAllDeletedArtists() {
		return ResponseEntity.ok(service.findAllDeleted());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ArtistResponseDTO> findById(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@GetMapping("/allByIds")
	public ResponseEntity<List<ArtistResponseDTO>> findAllByIds(@RequestParam List<Integer> ids) {
		return ResponseEntity.ok(service.findAllByids(ids));
	}
	
	@PostMapping("")
	public ResponseEntity<ArtistResponseDTO> saveArtist(@RequestBody @Valid ArtistRequestDTO request) throws NameAlreadyUsedException {
		return new ResponseEntity<>(service.saveArtist(request), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ArtistResponseDTO> updateArtist(@PathVariable Integer id, @RequestBody @Valid ArtistRequestDTO request) throws NotFoundException {
		return ResponseEntity.ok(service.updateArtist(id, request));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ArtistResponseDTO> deleteArtist(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.deleteArtist(id));
	}
	
	@DeleteMapping("/user/{userId}")
	public ResponseEntity<ArtistResponseDTO> deleteArtistByUserId(@PathVariable Integer userId) throws NotFoundException {
		return ResponseEntity.ok(service.deleteArtistByUserId(userId));
	}
}

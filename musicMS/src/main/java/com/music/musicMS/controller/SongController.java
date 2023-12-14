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

import com.music.musicMS.dto.SongRequestDTO;
import com.music.musicMS.dto.SongResponseDTO;
import com.music.musicMS.exception.NameAlreadyUsedException;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.exception.SomeEntityDoesNotExistException;
import com.music.musicMS.service.SongService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/song")
public class SongController {

	@Autowired
	private SongService service;
	
	//@GetMapping(value = "/search", params = {"name", "genres", "years"})
	@GetMapping("/search")
	public ResponseEntity<List<SongResponseDTO>> searchSongs(@RequestParam(required = false) String name, @RequestParam(required = false) List<String> genres, @RequestParam(required = false) List<Integer> years) {
		return ResponseEntity.ok(service.searchSongs(name, genres, years));
	}
	
	@GetMapping("")
	public ResponseEntity<List<SongResponseDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<SongResponseDTO> findById(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@PostMapping("")
	public ResponseEntity<SongResponseDTO> saveSong(@RequestBody @Valid SongRequestDTO request) throws NameAlreadyUsedException, SomeEntityDoesNotExistException {
		return new ResponseEntity<>(service.saveSong(request), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<SongResponseDTO> updateSong(@PathVariable Integer id, @RequestBody @Valid SongRequestDTO request) throws NotFoundException, SomeEntityDoesNotExistException {
		return ResponseEntity.ok(service.updateSong(id, request));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<SongResponseDTO> deleteSong(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.deleteSong(id));
	}
}

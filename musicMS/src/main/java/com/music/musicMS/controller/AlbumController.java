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
import org.springframework.web.bind.annotation.RestController;

import com.music.musicMS.dto.SongIdDTO;
import com.music.musicMS.dto.AlbumRequestDTO;
import com.music.musicMS.dto.AlbumResponseDTO;
import com.music.musicMS.dto.AlbumUpdateDTO;
import com.music.musicMS.exception.DoNotContainsTheSongException;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.exception.SomeEntityDoesNotExistException;
import com.music.musicMS.exception.SongIsAlreadyInAnAlbumException;
import com.music.musicMS.service.AlbumService;

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
	public ResponseEntity<AlbumResponseDTO> findById(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@PostMapping("")
	public ResponseEntity<AlbumResponseDTO> saveAlbum(@RequestBody @Valid AlbumRequestDTO request) {
		return new ResponseEntity<>(service.saveAlbum(request), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<AlbumResponseDTO> updateAlbum(@PathVariable Integer id, @RequestBody @Valid AlbumUpdateDTO request) throws NotFoundException, SomeEntityDoesNotExistException {
		return ResponseEntity.ok(service.updateAlbum(id, request));
	}
	
	@PutMapping("/{id}/addSong")
	public ResponseEntity<AlbumResponseDTO> addSong(@PathVariable Integer id, @RequestBody SongIdDTO request) throws NotFoundException, SongIsAlreadyInAnAlbumException {
		return ResponseEntity.ok(service.addSong(id, request));
	}
	
	@PutMapping("/{id}/removeSong")
	public ResponseEntity<AlbumResponseDTO> removeSong(@PathVariable Integer id, @RequestBody SongIdDTO request) throws NotFoundException, DoNotContainsTheSongException {
		return ResponseEntity.ok(service.removeSong(id, request));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<AlbumResponseDTO> deleteAlbum(@PathVariable int id) throws NotFoundException {
		return ResponseEntity.ok(service.deleteAlbum(id));
	}
}

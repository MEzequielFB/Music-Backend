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

import com.music.backend.dto.AddSongRequestDTO;
import com.music.backend.dto.PlaylistRequestDTO;
import com.music.backend.dto.PlaylistResponseDTO;
import com.music.backend.dto.PlaylistUpdateDTO;
import com.music.backend.dto.SongResponseDTO;
import com.music.backend.exception.AlreadyContainsSongException;
import com.music.backend.exception.NameAlreadyUsedException;
import com.music.backend.exception.NotFoundException;
import com.music.backend.service.PlaylistService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/playlist")
public class PlaylistController {
	
	@Autowired
	private PlaylistService service;
	
	@GetMapping("")
	public ResponseEntity<List<PlaylistResponseDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PlaylistResponseDTO> findById(@PathVariable int id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@PostMapping("")
	public ResponseEntity<PlaylistResponseDTO> savePlaylist(@RequestBody @Valid PlaylistRequestDTO request) throws NameAlreadyUsedException {
		return new ResponseEntity<>(service.savePlaylist(request), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<PlaylistResponseDTO> updatePlaylist(@PathVariable int id, @RequestBody @Valid PlaylistUpdateDTO request) throws NotFoundException {
		return ResponseEntity.ok(service.updatePlaylist(id, request));
	}
	
	@PutMapping("/{id}/song")
	public ResponseEntity<SongResponseDTO> addSong(@PathVariable int id, @RequestBody @Valid AddSongRequestDTO request) throws NotFoundException, AlreadyContainsSongException {
		return ResponseEntity.ok(service.addSong(id, request));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<PlaylistResponseDTO> deletePlaylist(@PathVariable int id) throws NotFoundException {
		return ResponseEntity.ok(service.deletePlaylist(id));
	}
}

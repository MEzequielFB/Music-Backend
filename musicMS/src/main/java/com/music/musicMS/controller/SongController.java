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

import com.music.musicMS.dto.SongRequestDTO;
import com.music.musicMS.dto.SongResponseDTO;
import com.music.musicMS.exception.AuthorizationException;
import com.music.musicMS.exception.NameAlreadyUsedException;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.exception.SomeEntityDoesNotExistException;
import com.music.musicMS.model.Roles;
import com.music.musicMS.service.SongService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/song")
public class SongController {

	@Autowired
	private SongService service;
	
	//@GetMapping(value = "/search", params = {"name", "genres", "years"})
	@GetMapping("/search")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<List<SongResponseDTO>> searchSongs(@RequestParam(required = false) String name, @RequestParam(required = false) List<String> genres, @RequestParam(required = false) List<Integer> years) {
		return ResponseEntity.ok(service.searchSongs(name, genres, years));
	}
	
	@GetMapping("")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<List<SongResponseDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<SongResponseDTO> findById(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@PostMapping("")
	@PreAuthorize("hasAuthority('" + Roles.ARTIST + "')")
	public ResponseEntity<SongResponseDTO> saveSong(@RequestBody @Valid SongRequestDTO request, @RequestHeader("Authorization") String token) throws NameAlreadyUsedException, SomeEntityDoesNotExistException, NotFoundException, AuthorizationException {
		return new ResponseEntity<>(service.saveSong(request, token), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('" + Roles.ARTIST + "')")
	public ResponseEntity<SongResponseDTO> updateSong(@PathVariable Integer id, @RequestBody @Valid SongRequestDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, SomeEntityDoesNotExistException, AuthorizationException {
		return ResponseEntity.ok(service.updateSong(id, request, token));
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<SongResponseDTO> deleteSong(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.deleteSong(id));
	}
}

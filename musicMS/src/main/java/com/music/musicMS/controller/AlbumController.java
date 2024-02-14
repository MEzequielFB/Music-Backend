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
import org.springframework.web.bind.annotation.RestController;

import com.music.musicMS.dto.SongIdDTO;
import com.music.musicMS.dto.AlbumRequestDTO;
import com.music.musicMS.dto.AlbumResponseDTO;
import com.music.musicMS.dto.AlbumUpdateDTO;
import com.music.musicMS.exception.AlbumOwnerNotInSongException;
import com.music.musicMS.exception.AuthorizationException;
import com.music.musicMS.exception.DoNotContainsTheSongException;
import com.music.musicMS.exception.NameAlreadyUsedException;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.exception.SomeEntityDoesNotExistException;
import com.music.musicMS.exception.SongIsAlreadyInAnAlbumException;
import com.music.musicMS.model.Roles;
import com.music.musicMS.service.AlbumService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/album")
public class AlbumController {

	@Autowired
	private AlbumService service;
	
	@GetMapping("")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<List<AlbumResponseDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<AlbumResponseDTO> findById(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@GetMapping("/owner/{ownerId}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<List<AlbumResponseDTO>> findAllByOwner(@PathVariable Integer ownerId) throws NotFoundException {
		return ResponseEntity.ok(service.findAllByOwner(ownerId));
	}
	
	@PostMapping("")
	@PreAuthorize("hasAuthority('" + Roles.ARTIST + "')")
	public ResponseEntity<AlbumResponseDTO> saveAlbum(@RequestBody @Valid AlbumRequestDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, NameAlreadyUsedException, AuthorizationException {
		return new ResponseEntity<>(service.saveAlbum(request, token), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<AlbumResponseDTO> updateAlbum(@PathVariable Integer id, @RequestBody @Valid AlbumUpdateDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, SomeEntityDoesNotExistException, AuthorizationException {
		return ResponseEntity.ok(service.updateAlbum(id, request, token));
	}
	
	@PutMapping("/{id}/addSong")
	@PreAuthorize("hasAuthority('" + Roles.ARTIST + "')")
	public ResponseEntity<AlbumResponseDTO> addSong(@PathVariable Integer id, @RequestBody @Valid SongIdDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, SongIsAlreadyInAnAlbumException, AlbumOwnerNotInSongException, AuthorizationException {
		return ResponseEntity.ok(service.addSong(id, request.getSongId(), token));
	}
	
	@PutMapping("/{id}/removeSong")
	@PreAuthorize("hasAuthority('" + Roles.ARTIST + "')")
	public ResponseEntity<AlbumResponseDTO> removeSong(@PathVariable Integer id, @RequestBody @Valid SongIdDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, DoNotContainsTheSongException, AuthorizationException {
		return ResponseEntity.ok(service.removeSong(id, request.getSongId(), token));
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<AlbumResponseDTO> deleteAlbum(@PathVariable Integer id, @RequestHeader("Authorization") String token) throws NotFoundException, AuthorizationException {
		return ResponseEntity.ok(service.deleteAlbum(id, token));
	}
}

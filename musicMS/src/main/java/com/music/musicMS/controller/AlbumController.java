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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/album")
@Tag(name = "Album Controller", description = "<p>Used for album management: create/delete/return albums, update albums attributes, add/remove songs to albums</p>")
public class AlbumController {

	@Autowired
	private AlbumService service;
	
	@Operation(summary = "Find all albums", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<List<AlbumResponseDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	// By name and artist name
	@Operation(summary = "Find albums by name and artist's name", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/search")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<List<AlbumResponseDTO>> findAllByFilter(@RequestParam(required = false) String data) {
		return ResponseEntity.ok(service.findAllByFilter(data));
	}
	
	@Operation(summary = "Find album by id", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<AlbumResponseDTO> findById(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@Operation(summary = "Find albums by owner (artist) id", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/owner/{ownerId}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<List<AlbumResponseDTO>> findAllByOwner(@PathVariable Integer ownerId) throws NotFoundException {
		return ResponseEntity.ok(service.findAllByOwner(ownerId));
	}
	
	@Operation(summary = "Save album", description = "<p>Required roles:</p> <ul><li>ARTIST</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@PostMapping("")
	@PreAuthorize("hasAuthority('" + Roles.ARTIST + "')")
	public ResponseEntity<AlbumResponseDTO> saveAlbum(@RequestBody @Valid AlbumRequestDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, NameAlreadyUsedException, AuthorizationException {
		return new ResponseEntity<>(service.saveAlbum(request, token), HttpStatus.CREATED);
	}
	
	@Operation(summary = "Update album. The logged artist should be the owner of the album", description = "<p>Required roles:</p> <ul><li>ARTIST</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('" + Roles.ARTIST + "')")
	public ResponseEntity<AlbumResponseDTO> updateAlbum(@PathVariable Integer id, @RequestBody @Valid AlbumUpdateDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, SomeEntityDoesNotExistException, AuthorizationException {
		return ResponseEntity.ok(service.updateAlbum(id, request, token));
	}
	
	@Operation(summary = "Add song to album. The logged artist should be the owner of the album, be part of the song and the album shouldn't contains the song already", description = "<p>Required roles:</p> <ul><li>ARTIST</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("/{id}/addSong")
	@PreAuthorize("hasAuthority('" + Roles.ARTIST + "')")
	public ResponseEntity<AlbumResponseDTO> addSong(@PathVariable Integer id, @RequestBody @Valid SongIdDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, SongIsAlreadyInAnAlbumException, AlbumOwnerNotInSongException, AuthorizationException {
		return ResponseEntity.ok(service.addSong(id, request.getSongId(), token));
	}
	
	@Operation(summary = "Remove song from album. The album should have the song and the logged artist should be de owner of the album", description = "<p>Required roles:</p> <ul><li>ARTIST</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("/{id}/removeSong")
	@PreAuthorize("hasAuthority('" + Roles.ARTIST + "')")
	public ResponseEntity<AlbumResponseDTO> removeSong(@PathVariable Integer id, @RequestBody @Valid SongIdDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, DoNotContainsTheSongException, AuthorizationException {
		return ResponseEntity.ok(service.removeSong(id, request.getSongId(), token));
	}
	
	@Operation(summary = "Delete album. The logged user should be the owner of the album  or be an administrator", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>ARTIST</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<AlbumResponseDTO> deleteAlbum(@PathVariable Integer id, @RequestHeader("Authorization") String token) throws NotFoundException, AuthorizationException {
		return ResponseEntity.ok(service.deleteAlbum(id, token));
	}
}

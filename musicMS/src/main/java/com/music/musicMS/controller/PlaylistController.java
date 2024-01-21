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

import com.music.musicMS.dto.PlaylistRequestDTO;
import com.music.musicMS.dto.PlaylistResponseDTO;
import com.music.musicMS.dto.PlaylistUpdateDTO;
import com.music.musicMS.dto.SongIdDTO;
import com.music.musicMS.dto.SongResponseDTO;
import com.music.musicMS.exception.AlreadyContainsSongException;
import com.music.musicMS.exception.NameAlreadyUsedException;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.model.Roles;
import com.music.musicMS.service.PlaylistService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/playlist")
public class PlaylistController {
	
	@Autowired
	private PlaylistService service;
	
	@GetMapping("")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<List<PlaylistResponseDTO>> findAll(@RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findAll(token));
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<PlaylistResponseDTO> findById(@PathVariable Integer id, @RequestHeader("Authorization") String token) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id, token));
	}
	
	@GetMapping("/{id}/songs")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<List<SongResponseDTO>> getSongsFromPlaylist(@PathVariable Integer id, @RequestHeader("Authorization") String token) throws NotFoundException {
		return ResponseEntity.ok(service.getSongsFromPlaylist(id, token));
	}
	
	@PostMapping("")
	@PreAuthorize("hasAuthority('" + Roles.USER + "')")
	public ResponseEntity<PlaylistResponseDTO> savePlaylist(@RequestBody @Valid PlaylistRequestDTO request, @RequestHeader("Authorization") String token) throws NameAlreadyUsedException, NotFoundException {
		return new ResponseEntity<>(service.savePlaylist(request, token), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.USER + "')")
	public ResponseEntity<PlaylistResponseDTO> updatePlaylist(@PathVariable Integer id, @RequestBody @Valid PlaylistUpdateDTO request, @RequestHeader("Authorization") String token) throws NotFoundException {
		return ResponseEntity.ok(service.updatePlaylist(id, request, token));
	}
	
	@PutMapping("/{id}/addSong")
	@PreAuthorize("hasAuthority('" + Roles.USER + "')")
	public ResponseEntity<SongResponseDTO> addSong(@PathVariable Integer id, @RequestBody @Valid SongIdDTO request) throws NotFoundException, AlreadyContainsSongException {
		return ResponseEntity.ok(service.addSong(id, request.getSongId()));
	}
	
	@PutMapping("/{id}/removeSong")
	@PreAuthorize("hasAuthority('" + Roles.USER + "')")
	public ResponseEntity<SongResponseDTO> removeSong(@PathVariable Integer id, @RequestBody @Valid SongIdDTO request) throws NotFoundException, AlreadyContainsSongException {
		return ResponseEntity.ok(service.removeSong(id, request.getSongId()));
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.USER + "')")
	public ResponseEntity<PlaylistResponseDTO> deletePlaylist(@PathVariable Integer id, @RequestHeader("Authorization") String token) throws NotFoundException {
		return ResponseEntity.ok(service.deletePlaylist(id, token));
	}
}

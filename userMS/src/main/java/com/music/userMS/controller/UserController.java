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

import com.music.userMS.dto.FollowRequestDTO;
import com.music.userMS.dto.UserFollowerResponseDTO;
import com.music.userMS.dto.UserRequestDTO;
import com.music.userMS.dto.UserResponseDTO;
import com.music.userMS.exception.EmailAlreadyUsedException;
import com.music.userMS.exception.NotFoundException;
import com.music.userMS.service.UserFollowerService;
import com.music.userMS.service.UserService;

import jakarta.validation.Valid;

@RestController(value = "userController")
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	private UserService service;
	
	@Autowired
	private UserFollowerService userFollowerService;
	
	@GetMapping("/{id}/followedUsers")
	public ResponseEntity<List<UserResponseDTO>> findFollowedUsersById(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.findFollowedUsersById(id));
	}
	
	@GetMapping("/{id}/followers")
	public ResponseEntity<List<UserResponseDTO>> findFollowersById(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.findFollowersById(id)); 
	}
	
	@PostMapping("/follow")
	public ResponseEntity<UserFollowerResponseDTO> followUser(@RequestBody @Valid FollowRequestDTO request) throws NotFoundException {
		return new ResponseEntity<>(userFollowerService.followUser(request), HttpStatus.CREATED);
	}
	
	@GetMapping("")
	public ResponseEntity<List<UserResponseDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserResponseDTO> findById(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@PostMapping("")
	public ResponseEntity<UserResponseDTO> saveUser(@RequestBody @Valid UserRequestDTO request) throws EmailAlreadyUsedException, NotFoundException {
		return new ResponseEntity<>(service.saveUser(request), HttpStatus.CREATED);
	}
	
	@PostMapping("/artist")
	public ResponseEntity<UserResponseDTO> saveArtistUser(@RequestBody @Valid UserRequestDTO request) throws EmailAlreadyUsedException, NotFoundException {
		return new ResponseEntity<>(service.saveArtistUser(request), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Integer id, @RequestBody @Valid UserRequestDTO request) throws NotFoundException, EmailAlreadyUsedException {
		return ResponseEntity.ok(service.updateUser(id, request));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<UserResponseDTO> deleteUser(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.deleteUser(id));
	}
}

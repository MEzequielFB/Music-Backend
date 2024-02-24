package com.music.authenticationMS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.music.authenticationMS.dto.AuthRequestDTO;
import com.music.authenticationMS.dto.AuthResponseDTO;
import com.music.authenticationMS.dto.UserRequestDTO;
import com.music.authenticationMS.exception.InvalidTokenException;
import com.music.authenticationMS.exception.NotFoundException;
import com.music.authenticationMS.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;

@RestController("authController")
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthService service;
	
	@Operation(summary = "Register an user", description = "Register an user. Returns token and user information")
	@PostMapping("/register")
	public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid UserRequestDTO request) throws NotFoundException {
		return ResponseEntity.ok(service.register(request));
	}

	@Operation(summary = "Register an artist", description = "Register an user with the artist role. It returns token and user's information")
	@PostMapping("/register/artist")
	public ResponseEntity<AuthResponseDTO> registerArtist(@RequestBody @Valid UserRequestDTO request) throws NotFoundException {
		return ResponseEntity.ok(service.registerArtist(request));
	}
	
	@Operation(summary = "Login to the application", description = "Enter the credentials, if they are correct returns the token and user's information")
	@PostMapping("/login")
	public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthRequestDTO request) throws NotFoundException {
		return ResponseEntity.ok(service.login(request));
	}
	
	@Operation(summary = "Validate token", description = "Returns if the token is valid or not")
	@GetMapping("/validate")
	public ResponseEntity<String> validate(@RequestParam String token) throws InvalidTokenException {
		return ResponseEntity.ok(service.validateToken(token));
	}
	
	@Operation(summary = "Get the id from the current logged user", description = "Get the id from the current logged user | Enter the token is required to work")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/id")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Integer> getLoggedUserId() throws NotFoundException {
		return ResponseEntity.ok(service.getLoggedUserId());
	}
}

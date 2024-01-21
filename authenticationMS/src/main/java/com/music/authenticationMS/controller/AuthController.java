package com.music.authenticationMS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.music.authenticationMS.dto.AuthRequestDTO;
import com.music.authenticationMS.dto.UserRequestDTO;
import com.music.authenticationMS.exception.InvalidTokenException;
import com.music.authenticationMS.exception.NotFoundException;
import com.music.authenticationMS.service.AuthService;

import jakarta.validation.Valid;

@RestController("authController")
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthService service;
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody @Valid UserRequestDTO request) throws NotFoundException {
		return ResponseEntity.ok(service.register(request));
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody @Valid AuthRequestDTO request) throws NotFoundException {
		return ResponseEntity.ok(service.login(request));
	}
	
	@GetMapping("/validate")
	public ResponseEntity<String> validate(@RequestParam String token) throws InvalidTokenException {
		return ResponseEntity.ok(service.validateToken(token));
	}
}

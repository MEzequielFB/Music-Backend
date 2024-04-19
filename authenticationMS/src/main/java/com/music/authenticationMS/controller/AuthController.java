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
import com.music.authenticationMS.exception.AuthenticationException;
import com.music.authenticationMS.exception.InvalidTokenException;
import com.music.authenticationMS.exception.NotFoundException;
import com.music.authenticationMS.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController("authController")
@RequestMapping("/api/auth")
@Tag(name = "Authentication Controller", description = "<p>Used for authentication, user/artist registry and token validation</p>")
public class AuthController {

	@Autowired
	private AuthService service;
	
	@Operation(summary = "Register an user", description = "Register an user. Returns token and user information")
	@ApiResponses(value = {
		@ApiResponse(responseCode =  "200", description = "User register successfully", content = {
			@Content(schema = @Schema(implementation = AuthResponseDTO.class))
		}),
		@ApiResponse(responseCode =  "401", description = "Register failed so the user with the email entered doesn't exist", content = {
			@Content(schema = @Schema(example = "Invalid email or password"))
		}),
		@ApiResponse(responseCode =  "400", description = "The email entered is already in use", content = {
			@Content(schema = @Schema(example = "the email 'user1@gmail.com' is already in use"))
		})
	})
	@PostMapping("/register")
	public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid UserRequestDTO request) throws AuthenticationException {
		return ResponseEntity.ok(service.register(request));
	}

	@Operation(summary = "Register an artist", description = "Register an user with the artist role. It returns token and user's information")
	@ApiResponses(value = {
		@ApiResponse(responseCode =  "200", description = "User register successfully", content = {
			@Content(schema = @Schema(implementation = AuthResponseDTO.class))
		}),
		@ApiResponse(responseCode =  "401", description = "Register failed so the user with the email entered doesn't exist", content = {
			@Content(schema = @Schema(example = "Invalid email or password"))
		}),
		@ApiResponse(responseCode =  "400", description = "The email entered is already in use", content = {
			@Content(schema = @Schema(example = "The email 'user1@gmail.com' is already in use"))
		})
	})
	@PostMapping("/register/artist")
	public ResponseEntity<AuthResponseDTO> registerArtist(@RequestBody @Valid UserRequestDTO request) throws AuthenticationException {
		return ResponseEntity.ok(service.registerArtist(request));
	}
	
	@Operation(summary = "Login to the application", description = "Enter the credentials, if they are correct returns the token and user's information")
	@ApiResponses(value = {
		@ApiResponse(responseCode =  "200", description = "User logged successfully", content = {
			@Content(schema = @Schema(implementation = AuthResponseDTO.class))
		}),
		@ApiResponse(responseCode =  "401", description = "The credentials entered are invalid", content = {
			@Content(schema = @Schema(example = "Invalid email or password"))
		})
	})
	@PostMapping("/login")
	public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthRequestDTO request) throws  AuthenticationException {
		return ResponseEntity.ok(service.login(request));
	}
	
	@Operation(summary = "Validate token", description = "Returns if the token is valid or not", 
			parameters = {
				@Parameter(name = "token", description = "Provided token when loggin in or registering", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode =  "200", description = "OK", content = {
			@Content(schema = @Schema(example = "The token is valid"))
		}),
		@ApiResponse(responseCode =  "401", description = "Invalid token!", content = {
			@Content(schema = @Schema(example = "Invalid token"))
		})
	})
	@GetMapping("/validate")
	public ResponseEntity<String> validate(@RequestParam String token) throws InvalidTokenException {
		try {
			return ResponseEntity.ok(service.validateToken(token));
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Operation(summary = "Get the id from the current logged user", description = "Get the id from the current logged user | Enter the token is required to work")
	@ApiResponses(value = {
		@ApiResponse(responseCode =  "200", description = "OK", content = {
			@Content(schema = @Schema(example = "1"))
		}),
		@ApiResponse(responseCode =  "404", description = "User not found", content = {
			@Content(schema = @Schema(example = "No authenticated user available"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/id")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Integer> getLoggedUserId() throws NotFoundException {
		return ResponseEntity.ok(service.getLoggedUserId());
	}
}

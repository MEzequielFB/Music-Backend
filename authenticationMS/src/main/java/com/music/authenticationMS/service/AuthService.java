package com.music.authenticationMS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.music.authenticationMS.dto.AuthRequestDTO;
import com.music.authenticationMS.dto.UserDTO;
import com.music.authenticationMS.dto.UserRequestDTO;
import com.music.authenticationMS.exception.InvalidTokenException;
import com.music.authenticationMS.exception.NotFoundException;

@Service("authService")
public class AuthService {
	
	@Autowired
	private JwtService jwtService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	public String register(UserRequestDTO request) {
		request.setPassword(passwordEncoder.encode(request.getPassword()));
		
		UserDTO user = webClientBuilder.build()
			.post()
			.uri("http://localhost:8001/api/user")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(request)
			.retrieve()
			.bodyToMono(UserDTO.class)
			.block();
		
		return jwtService.generateToken(user.getEmail());
	}
	
	public String login(AuthRequestDTO request) throws NotFoundException {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		if (authentication.isAuthenticated()) {
			return jwtService.generateToken(request.getEmail());
		} else {
			throw new NotFoundException("User", request.getEmail());
		}
	}
	
	public void validateToken(String token) throws InvalidTokenException {
		jwtService.validateToken(token);
	}
}

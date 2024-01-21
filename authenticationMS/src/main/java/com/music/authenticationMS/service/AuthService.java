package com.music.authenticationMS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.music.authenticationMS.dto.AuthRequestDTO;
import com.music.authenticationMS.dto.UserDTO;
import com.music.authenticationMS.dto.UserRequestDTO;
import com.music.authenticationMS.exception.InvalidTokenException;
import com.music.authenticationMS.exception.NotFoundException;
import com.music.authenticationMS.security.TokenProvider;

@Service("authService")
public class AuthService {
	
	@Autowired
	private TokenProvider tokenProvider;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Transactional
	public String register(UserRequestDTO request) throws NotFoundException {
		String decodePassword = request.getPassword();
		request.setPassword(passwordEncoder.encode(request.getPassword()));
		
		UserDTO user = webClientBuilder.build()
			.post()
			.uri("http://localhost:8001/api/user")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(request)
			.retrieve()
			.bodyToMono(UserDTO.class)
			.block();
		
		return login(new AuthRequestDTO(user.getEmail(), decodePassword));
	}
	
	@Transactional
	public String login(AuthRequestDTO request) throws NotFoundException {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())); 
		if (authentication.isAuthenticated()) {
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = tokenProvider.createToken(authentication);
			
			return jwt;
		} else {
			throw new NotFoundException("User", request.getEmail());
		}
	}
	
	@Transactional
	public String validateToken(String token) throws InvalidTokenException {
		Boolean isValid = tokenProvider.validateToken(token);
		if (isValid) {
			return "The token is valid";
		}
		return "Invalid token!";
	}
}

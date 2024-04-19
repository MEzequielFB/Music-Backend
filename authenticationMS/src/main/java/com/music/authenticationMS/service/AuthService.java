package com.music.authenticationMS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.music.authenticationMS.config.CustomUserDetails;
import com.music.authenticationMS.dto.AuthRequestDTO;
import com.music.authenticationMS.dto.AuthResponseDTO;
import com.music.authenticationMS.dto.UserDTO;
import com.music.authenticationMS.dto.UserRequestDTO;
import com.music.authenticationMS.exception.AuthenticationException;
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
	private WebClient webClient;
	
	@Value("${app.api.domain}")
	private String domain;
	
	@Value("${app.api.userms.domain}")
	private String usermsDomain;
	
	@Transactional
	public AuthResponseDTO register(UserRequestDTO request) throws AuthenticationException {
		String decodePassword = request.getPassword();
		request.setPassword(passwordEncoder.encode(request.getPassword()));
		
		UserDTO user = webClient
			.post()
			.uri(String.format("%s/api/user", this.usermsDomain))
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(request)
			.retrieve()
			.bodyToMono(UserDTO.class)
			.block();
			
		return login(new AuthRequestDTO(user.getEmail(), decodePassword));
	}
	
	@Transactional
	public AuthResponseDTO registerArtist(UserRequestDTO request) throws AuthenticationException {
		String decodePassword = request.getPassword();
		request.setPassword(passwordEncoder.encode(request.getPassword()));
		
		UserDTO user = webClient
				.post()
				.uri(String.format("%s/api/user/artist", this.usermsDomain))
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.retrieve()
				.bodyToMono(UserDTO.class)
				.block();
		
		return login(new AuthRequestDTO(user.getEmail(), decodePassword));
	}
	
	@Transactional
	public AuthResponseDTO login(AuthRequestDTO request) throws AuthenticationException {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())); 
		if (authentication.isAuthenticated()) {
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
	        System.out.println("Authentication object after setting in SecurityContextHolder: {} " + authentication);
			
			CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
			String jwt = tokenProvider.createToken(authentication, user.getId());

			return new AuthResponseDTO(new UserDTO(user), jwt);
		} else {
			throw new AuthenticationException();
		}
	}
	
	@Transactional
	public String validateToken(String token) throws InvalidTokenException {
		Boolean	isValid = tokenProvider.validateToken(token);
		if (isValid) {
			return "The token is valid";
		}
		throw new InvalidTokenException();
	}
	
	@Transactional
	public Integer getLoggedUserId() throws NotFoundException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			System.out.println("Authentication object after setting in SecurityContextHolder: {} " + authentication);
			
			CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
			return user.getId();
		} else {
			throw new NotFoundException();
		}
	}
}

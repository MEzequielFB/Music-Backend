package com.music.authenticationMS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.reactive.function.client.WebClient;

import com.music.authenticationMS.config.CustomUserDetails;
import com.music.authenticationMS.dto.UserDTO;

public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private WebClient.Builder webClientBuilder;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		try {
			UserDTO user = webClientBuilder.build()
					.get()
					.uri("http://localhost:8001/api/user/email" + email)
					.retrieve()
					.bodyToMono(UserDTO.class)
					.block();
			
			return new CustomUserDetails(user);
		} catch (Exception e) {
			throw new UsernameNotFoundException(String.format("An user with email %s does not exists", email));
		}
	}

}

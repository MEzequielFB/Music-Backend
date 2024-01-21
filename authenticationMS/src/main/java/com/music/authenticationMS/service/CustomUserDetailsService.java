package com.music.authenticationMS.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.reactive.function.client.WebClient;

import com.music.authenticationMS.dto.UserDTO;

public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private WebClient.Builder webClientBuilder;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		try {
			UserDTO user = webClientBuilder.build()
					.get()
					.uri("http://localhost:8001/api/user/email/" + email)
					.retrieve()
					.bodyToMono(UserDTO.class)
					.block();
			
			return createSpringSecurityUser(user);
		} catch (Exception e) {
			throw new UsernameNotFoundException(String.format("An user with email %s does not exists", email));
		}
	}

	private User createSpringSecurityUser(UserDTO user) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRole());
        List<GrantedAuthority> grantedAuthorities = Collections.singletonList(grantedAuthority);

        return new User(user.getEmail(), user.getPassword(), grantedAuthorities);
    }
}

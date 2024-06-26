package com.music.authenticationMS.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.reactive.function.client.WebClient;

import com.music.authenticationMS.config.CustomUserDetails;
import com.music.authenticationMS.dto.UserDTO;

public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private WebClient webClient;
	
	@Value("${app.api.domain}")
	private String domain;
	
	@Value("${app.api.userms.domain}")
	private String usermsDomain;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserDTO user = webClient
				.get()
				.uri(String.format("%s/api/user/email/%s", this.usermsDomain, email))
				.retrieve()
				.bodyToMono(UserDTO.class)
				.block();
		
		return createSpringSecurityUser(user);
	}

	private UserDetails createSpringSecurityUser(UserDTO user) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRole());
        List<GrantedAuthority> grantedAuthorities = Collections.singletonList(grantedAuthority);

        return new CustomUserDetails(user, grantedAuthorities);
//        return new User(user.getEmail(), user.getPassword(), grantedAuthorities);
    }
}

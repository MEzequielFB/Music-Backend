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
	private WebClient.Builder webClientBuilder;
	
	@Value("${app.api.domain}")
	private String domain;
	
	@Value("${app.api.userms.port}")
	private String usermsPort;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		try {
			UserDTO user = webClientBuilder.build()
					.get()
					.uri(String.format("%s:%s/api/user/email/%s", this.domain, this.usermsPort, email))
					.retrieve()
					.bodyToMono(UserDTO.class)
					.block();
			
			return createSpringSecurityUser(user);
		} catch (Exception e) {
			throw new UsernameNotFoundException(String.format("An user with email %s does not exists", email));
		}
	}

	private UserDetails createSpringSecurityUser(UserDTO user) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRole());
        List<GrantedAuthority> grantedAuthorities = Collections.singletonList(grantedAuthority);

        return new CustomUserDetails(user, grantedAuthorities);
//        return new User(user.getEmail(), user.getPassword(), grantedAuthorities);
    }
}

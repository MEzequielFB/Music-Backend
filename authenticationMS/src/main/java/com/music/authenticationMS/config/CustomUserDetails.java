package com.music.authenticationMS.config;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.music.authenticationMS.dto.UserDTO;


@SuppressWarnings("serial")
public class CustomUserDetails extends User {
	private Integer id;
	
	public CustomUserDetails(UserDTO user, Collection<? extends GrantedAuthority> grantedAuthorities) {
		super(user.getEmail(), user.getPassword(), grantedAuthorities);
		this.id = user.getId();
	}
	
	public CustomUserDetails(Integer id, String subject, Collection<? extends GrantedAuthority> grantedAuthorities) {
		super(subject, "", grantedAuthorities);
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
}

package com.music.authenticationMS.config;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.music.authenticationMS.dto.UserDTO;


@SuppressWarnings("serial")
public class CustomUserDetails extends User /*implements UserDetails*/ {
	private Integer id;
//	private String email;
//	private String password;
//	private String role;
	
	public CustomUserDetails(UserDTO user, Collection<? extends GrantedAuthority> grantedAuthorities) {
		super(user.getEmail(), user.getPassword(), grantedAuthorities);
		this.id = user.getId();
	}
	
	public Integer getId() {
		return this.id;
	}
	
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	@Override
//	public String getPassword() {
//		return password;
//	}
//
//	@Override
//	public String getUsername() {
//		return email;
//	}

//	@Override
//	public boolean isAccountNonExpired() {
//		return true;
//	}
//
//	@Override
//	public boolean isAccountNonLocked() {
//		return true;
//	}
//
//	@Override
//	public boolean isCredentialsNonExpired() {
//		return true;
//	}
//
//	@Override
//	public boolean isEnabled() {
//		return true;
//	}

}

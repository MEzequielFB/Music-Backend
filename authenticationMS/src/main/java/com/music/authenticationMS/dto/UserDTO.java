package com.music.authenticationMS.dto;

import org.springframework.security.core.userdetails.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private String email;
	private String password;
	private String role;
	
	public UserDTO(User user) {
		this.email = user.getUsername();
		this.password = null;
		this.role = user.getAuthorities().toArray()[0].toString();
	}
}

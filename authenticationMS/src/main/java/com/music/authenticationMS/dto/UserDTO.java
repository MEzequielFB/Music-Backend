package com.music.authenticationMS.dto;

import com.music.authenticationMS.config.CustomUserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private Integer id;
	private String email;
	private String password;
	private String role;
	
	public UserDTO(CustomUserDetails user) {
		this.id = user.getId();
		this.email = user.getUsername();
		this.password = null;
		this.role = user.getAuthorities().toArray()[0].toString();
	}
}

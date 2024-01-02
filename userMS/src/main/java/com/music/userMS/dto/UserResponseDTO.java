package com.music.userMS.dto;

import com.music.userMS.model.Role;
import com.music.userMS.model.User;

import lombok.Data;

@Data
public class UserResponseDTO {
	private Integer id;
	private String username;
	private String email;
	private Role role;
	
	public UserResponseDTO(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.role = user.getRole();
	}
}

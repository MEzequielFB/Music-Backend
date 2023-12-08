package com.music.backend.dto;

import com.music.backend.model.User;

import lombok.Data;

@Data
public class UserResponseDTO {
	private int id;
	private String username;
	private String email;
	private int role;
	
	public UserResponseDTO(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.role = user.getRole();
	}
}

package com.music.userMS.dto;

import com.music.userMS.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsResponseDTO {
	private Integer id;
	private String email;
	private String password;
	private String role;
	
	public UserDetailsResponseDTO(User user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.role = user.getRole().getName();
	}
}

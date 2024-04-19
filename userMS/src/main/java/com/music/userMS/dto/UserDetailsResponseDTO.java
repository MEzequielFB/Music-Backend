package com.music.userMS.dto;

import com.music.userMS.model.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsResponseDTO {
	@Schema(name = "id", example = "1")
	private Integer id;
	
	@Schema(name = "email", example = "user1@gmail.com")
	private String email;
	
	@Schema(name = "password", example = "fja9s8f92jf19js9fi1ja9f8h291 (encrypted password)")
	private String password;
	
	@Schema(name = "role", example = "USER")
	private String role;
	
	public UserDetailsResponseDTO(User user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.role = user.getRole().getName();
	}
}

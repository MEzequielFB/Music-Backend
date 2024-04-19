package com.music.authenticationMS.dto;

import com.music.authenticationMS.config.CustomUserDetails;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	@Schema(name = "id", example = "1")
	private Integer id;
	
	@Schema(name = "email", example = "user1@gmail.com")
	private String email;
	
	@Schema(name = "password", example = "null")
	private String password;
	
	@Schema(name = "role", example = "USER")
	private String role;
	
	public UserDTO(CustomUserDetails user) {
		this.id = user.getId();
		this.email = user.getUsername();
		this.password = null;
		this.role = user.getAuthorities().toArray()[0].toString();
	}
}

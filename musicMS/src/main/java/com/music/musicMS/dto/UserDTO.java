package com.music.musicMS.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	@Schema(name = "id", example = "1")
	private Integer id;
	
	@Schema(name = "username", example = "user1")
	private String username;
	
	@Schema(name = "email", example = "user1@gmail.com")
	private String email;
	
	@Schema(name = "role", example = "USER")
	private String role;
}

package com.music.userMS.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequestDTO {
	@NotNull(message = "username shouldn't be null")
	@NotBlank(message = "username shouldn't be empty")
	private String username;
	
	@NotNull(message = "email shouldn't be null")
	@NotBlank(message = "email shouldn't be empty")
	@Email(message = "invalid email")
	private String email;
	
	@NotNull(message = "password shouldn't be null")
	@NotBlank(message = "password shouldn't be empty")
	private String password;
	
	@NotNull(message = "role shouldn't be null")
	@NotBlank(message = "password shouldn't be blank")
	private String role;
}

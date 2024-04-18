package com.music.authenticationMS.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequestDTO {
	@NotNull(message = "username shouldn't be null")
	@NotBlank(message = "username shouldn't be empty")
	@Schema(name = "username", example = "user1")
	private String username;
	
	@NotNull(message = "email shouldn't be null")
	@NotBlank(message = "email shouldn't be empty")
	@Email(message = "invalid email")
	@Schema(name = "email", example = "user1@gmail.com")
	private String email;
	
	@NotNull(message = "address shouldn't be null")
	@NotBlank(message = "address shouldn't be empty")
	@Schema(name = "address", example = "street 2824")
	private String address;
	
	@NotNull(message = "password shouldn't be null")
	@NotBlank(message = "password shouldn't be empty")
	@Schema(name = "password", example = "password123")
	private String password;
}

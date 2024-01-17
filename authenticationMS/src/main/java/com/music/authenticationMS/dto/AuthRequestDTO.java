package com.music.authenticationMS.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestDTO {
	
	@NotNull(message = "email shouldn't be null")
	@NotBlank(message = "email shouldn't be blank")
	@Email(message = "email doesn't have an email format")
	private String email;
	
	@NotNull(message = "password shouldn't be null")
	@NotBlank(message = "password shouldn't be blank")
	private String password;
}

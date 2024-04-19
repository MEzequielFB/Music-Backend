package com.music.authenticationMS.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
	@Schema(implementation = UserDTO.class)
	private UserDTO user;
	
	@Schema(name = "token", example = "ksaf81smnmvb373ncadsvuewqndbq2")
	private String token;
}

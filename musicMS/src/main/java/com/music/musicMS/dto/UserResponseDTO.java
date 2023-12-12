package com.music.musicMS.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponseDTO {
	private int id;
	private String username;
	private String email;
	private int role;
}

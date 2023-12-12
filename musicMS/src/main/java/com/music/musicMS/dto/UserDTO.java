package com.music.musicMS.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
	private Integer id;
	private String username;
	private String email;
	private Integer role;
}

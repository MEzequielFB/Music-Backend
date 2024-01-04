package com.music.userMS.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Roles {
	public static final String USER = "USER";
	public static final String ARTIST = "ARTIST";
	public static final String ADMIN = "ADMIN";
}

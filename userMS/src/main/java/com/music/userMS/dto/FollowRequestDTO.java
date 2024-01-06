package com.music.userMS.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FollowRequestDTO {
	@NotNull(message = "userId shouldn't be null")
	private int userId;
	
	@NotNull(message = "followedUserId shouldn't be null")
	private int followedUserId;
}

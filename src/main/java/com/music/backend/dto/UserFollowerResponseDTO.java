package com.music.backend.dto;

import com.music.backend.model.UserFollowers;

import lombok.Data;

@Data
public class UserFollowerResponseDTO {
	private int userId;
	private int followerId;
	
	public UserFollowerResponseDTO(UserFollowers userFollower) {
		this.userId = userFollower.getUser().getId();
		this.followerId = userFollower.getFollower().getId();
	}
}

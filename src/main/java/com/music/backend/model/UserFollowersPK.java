package com.music.backend.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@SuppressWarnings("serial")
@Embeddable
@Data
public class UserFollowersPK implements Serializable {
	@Column(name = "user_id")
	private int userId;
	
	@Column(name = "follower_id")
	private int followerId;
}

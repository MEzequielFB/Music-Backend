package com.music.userMS.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Embeddable
@Data
@NoArgsConstructor
public class UserFollowersPK implements Serializable {
	@Column(name = "user_id")
	private int userId;
	
	@Column(name = "follower_id")
	private int followerId;
}

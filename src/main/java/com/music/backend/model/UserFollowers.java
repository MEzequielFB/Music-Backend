package com.music.backend.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class UserFollowers {
	@EmbeddedId
	private UserFollowersPK id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userId")
	@JoinColumn(name="user_id")
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("followerId")
	@JoinColumn(name="follower_id")
	private User follower;
	
	@Column(nullable = false)
	private Date followDatetime;
	
	@Column
	private Date unfollowDatetime;
	
	public UserFollowers(User user, User follower, Date followeDatetime) {
		this.id = new UserFollowersPK();
		this.user = user;
		this.follower = follower;
		this.followDatetime = followeDatetime;
	}
}

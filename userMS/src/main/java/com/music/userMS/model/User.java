package com.music.userMS.model;

import java.util.List;

import com.music.userMS.dto.UserRequestDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private String username;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String address;
	
	@Column(nullable = false)
	private Boolean isDeleted;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Role role;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "user")
	private List<UserFollowers> followers;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "follower")
	private List<UserFollowers> followedUsers;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
	private List<Account> accounts;
	
	public User(UserRequestDTO request, Role role) {
		this.username = request.getUsername();
		this.email = request.getEmail();
		this.password = request.getPassword();
		this.address = request.getAddress();
		this.isDeleted = false;
		this.role = role;
	}
}

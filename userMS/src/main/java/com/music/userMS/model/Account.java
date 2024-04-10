package com.music.userMS.model;

import java.util.List;
import java.util.Set;

import com.music.userMS.dto.AccountRequestDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			joinColumns = {@JoinColumn(name = "account_id")},
			inverseJoinColumns = {@JoinColumn(name = "user_id")})
	private Set<User> users;
	
	@Column(nullable = false)
	private Double balance;
	
	public Account(AccountRequestDTO request, Set<User> users) {
		this.users = users;
		this.balance = request.getBalance();
	}
	
	public Boolean containsUser(User user) {
		return users.contains(user);
	}
	
	public void addUser(User user) {
		users.add(user);
	}
	
	public void removeUser(User user) {
		users.remove(user);
	}
}

package com.music.userMS.dto;

import java.util.List;

import com.music.userMS.model.Account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDTO {
	private Integer id;
	private List<UserResponseDTO> users;
	private Double balance;
	
	public AccountResponseDTO(Account account) {
		this.id = account.getId();
		this.users = account.getUsers().stream().map( UserResponseDTO::new ).toList();
		this.balance = account.getBalance();
	}
}

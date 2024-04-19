package com.music.userMS.dto;

import java.util.List;

import com.music.userMS.model.Account;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDTO {
	@Schema(name = "id", example = "1")
	private Integer id;
	
	@ArraySchema(schema = @Schema(implementation = UserResponseDTO.class))
	private List<UserResponseDTO> users;
	
	@Schema(name = "balance", example = "1500")
	private Double balance;
	
	public AccountResponseDTO(Account account) {
		this.id = account.getId();
		this.users = account.getUsers().stream().map( UserResponseDTO::new ).toList();
		this.balance = account.getBalance();
	}
}

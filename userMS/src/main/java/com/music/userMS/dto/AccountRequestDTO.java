package com.music.userMS.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequestDTO {

	@NotNull(message = "usersId shouldn't be null")
	@NotEmpty(message = "usersId shouldn't be empty")
	private List<Integer> usersId;
	
	@NotNull(message = "balance shouldn't be null")
	@PositiveOrZero(message = "balance should be positive or zero")
	private Double balance;
}

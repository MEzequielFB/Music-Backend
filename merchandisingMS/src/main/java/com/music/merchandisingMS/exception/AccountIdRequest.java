package com.music.merchandisingMS.exception;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountIdRequest {

	@NotNull(message = "accountId shouldn't be null")
	@Positive(message = "accountId should be positive")
	private Integer accountId;
}

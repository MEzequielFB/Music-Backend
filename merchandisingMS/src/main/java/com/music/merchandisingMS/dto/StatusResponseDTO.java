package com.music.merchandisingMS.dto;

import com.music.merchandisingMS.model.Status;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusResponseDTO {
	@Schema(name = "id", example = "1")
	private Integer id;
	
	@Schema(name = "name", example = "status1")
	private String name;
	
	public StatusResponseDTO(Status status) {
		this.id = status.getId();
		this.name = status.getName();
	}
}

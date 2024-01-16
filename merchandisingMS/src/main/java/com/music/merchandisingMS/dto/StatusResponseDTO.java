package com.music.merchandisingMS.dto;

import com.music.merchandisingMS.model.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusResponseDTO {
	private Integer id;
	private String name;
	
	public StatusResponseDTO(Status status) {
		this.id = status.getId();
		this.name = status.getName();
	}
}

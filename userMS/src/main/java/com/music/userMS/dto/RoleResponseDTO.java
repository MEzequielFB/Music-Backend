package com.music.userMS.dto;

import com.music.userMS.model.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponseDTO {
	private Integer id;
	private String name;
	
	public RoleResponseDTO(Role role) {
		this.id = role.getId();
		this.name = role.getName();
	}
}

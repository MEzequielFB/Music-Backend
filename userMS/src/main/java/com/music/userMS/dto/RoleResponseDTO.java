package com.music.userMS.dto;

import com.music.userMS.model.Role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponseDTO {
	@Schema(name = "id", example = "1")
	private Integer id;
	
	@Schema(name = "name", example = "USER")
	private String name;
	
	public RoleResponseDTO(Role role) {
		this.id = role.getId();
		this.name = role.getName();
	}
}

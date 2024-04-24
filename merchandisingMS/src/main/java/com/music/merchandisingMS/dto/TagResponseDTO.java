package com.music.merchandisingMS.dto;

import com.music.merchandisingMS.model.Tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagResponseDTO {
	@Schema(name = "id", example = "1")
	private Integer id;
	
	@Schema(name = "name", example = "tag1")
	private String name;
	
	public TagResponseDTO(Tag tag) {
		this.id = tag.getId();
		this.name = tag.getName();
	}
}

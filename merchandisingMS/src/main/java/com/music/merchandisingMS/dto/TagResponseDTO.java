package com.music.merchandisingMS.dto;

import com.music.merchandisingMS.model.Tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagResponseDTO {
	private Integer id;
	private String name;
	
	public TagResponseDTO(Tag tag) {
		this.id = tag.getId();
		this.name = tag.getName();
	}
}

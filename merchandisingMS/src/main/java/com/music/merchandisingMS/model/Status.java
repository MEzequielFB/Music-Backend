package com.music.merchandisingMS.model;

import java.util.List;

import com.music.merchandisingMS.dto.StatusRequestDTO;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Status {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private String name;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
	private List<Order> orders;
	
	public Status(StatusRequestDTO request) {
		this.name = request.getName();
	}
}

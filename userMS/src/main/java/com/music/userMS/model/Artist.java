package com.music.userMS.model;

import com.music.userMS.dto.ArtistRequestDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Artist {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String password;
	
	public Artist(ArtistRequestDTO request) {
		this.name = request.getName();
		this.password = request.getPassword();
	}
}

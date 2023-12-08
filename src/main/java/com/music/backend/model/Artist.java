package com.music.backend.model;

import java.util.List;

import com.music.backend.dto.ArtistRequestDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
public class Artist {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String password;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "artists")
	private List<Album> albums;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "artists")
	private List<Song> songs;
	
	public Artist(ArtistRequestDTO request) {
		this.name = request.getName();
		this.password = request.getPassword();
	}
}

package com.music.musicMS.model;

import java.util.List;

import com.music.musicMS.dto.GenreRequestDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false)
	private String name;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "genres")
	private List<Song> songs;
	
	public Genre(GenreRequestDTO request) {
		this.name = request.getName();
	}
}

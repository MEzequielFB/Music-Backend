package com.music.musicMS.model;

import java.util.List;

import com.music.musicMS.dto.AlbumRequestDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Album {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private List<Integer> artists; // ids
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "album")
	private List<Song> songs;
	
	public Album(AlbumRequestDTO request) {
		this.name = request.getName();
		this.artists = request.getArtists();
	}
}

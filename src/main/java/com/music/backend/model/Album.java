package com.music.backend.model;

import java.util.List;

import com.music.backend.dto.AlbumRequestDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Album {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false)
	private String name;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			joinColumns = {@JoinColumn(name = "album_id")},
			inverseJoinColumns = {@JoinColumn(name = "artist_id")})
	private List<Artist> artists;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "album")
	private List<Song> songs;
	
	public Album(AlbumRequestDTO request) {
		this.name = request.getName();
		this.artists = request.getArtists();
	}
}

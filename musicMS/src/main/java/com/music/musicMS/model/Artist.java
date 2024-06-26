package com.music.musicMS.model;

import java.util.List;

import com.music.musicMS.dto.ArtistRequestDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Artist {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(unique = true)
	private Integer userId;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private Boolean isDeleted;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "artists")
	private List<Song> songs;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "artists")
	private List<Album> albums;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
	private List<Album> ownedAlbums;
	
	public Artist(ArtistRequestDTO request) {
		this.userId = request.getUserId();
		this.name = request.getName();
		this.isDeleted = false;
	}
	
	public void addAlbum(Album album) {
		albums.add(album);
	}
	
	public void removeAlbum(Album album) {
		albums.remove(album);
	}
}

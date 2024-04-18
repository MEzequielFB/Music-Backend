package com.music.musicMS.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.music.musicMS.dto.AlbumRequestDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Album {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false)
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Artist owner;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			joinColumns = {@JoinColumn(name = "album_id")},
			inverseJoinColumns = {@JoinColumn(name = "artist_id")})
	private Set<Artist> artists = new HashSet<>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "album")
	private List<Song> songs = new ArrayList<>();
	
	public Album(AlbumRequestDTO request) {
		this.name = request.getName();
	}
	
	// Method used to show the songs in an album response
	public void addSong(Song song) {
		songs.add(song);
	}
	
	// Method used to show the songs in an album response
	public void removeSong(Song song) {
		songs.remove(song);
	}
	
	// Method used to show the artists in an album response
	public void addArtist(Artist artist) {
		artists.add(artist);
	}
}

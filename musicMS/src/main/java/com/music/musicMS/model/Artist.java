package com.music.musicMS.model;

import java.util.List;

import com.music.musicMS.dto.ArtistRequestDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "artists")
	private List<Song> songs;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			joinColumns = {@JoinColumn(name = "artist_id")},
			inverseJoinColumns = {@JoinColumn(name = "album_id")})
	private List<Album> albums;
	
	public Artist(ArtistRequestDTO request) {
		this.name = request.getName();
		this.password = request.getPassword();
	}
	
	public void addSong(Song song) {
		this.songs.add(song);
	}
}

package com.music.musicMS.model;

import java.sql.Date;
import java.util.List;

import com.music.musicMS.dto.SongRequestDTO;

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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Song {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private Date releaseDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Album album;
	
	@Column(nullable = false)
	private List<Integer> artists;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			joinColumns = {@JoinColumn(name = "song_id")},
			inverseJoinColumns = {@JoinColumn(name = "genre_id")})
	private List<Genre> genres;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "songs")
	private List<Playlist> playlists;
	
	public Song(SongRequestDTO request) {
		this.name = request.getName();
		this.album = request.getAlbum();
		this.artists = request.getArtists();
	}
}

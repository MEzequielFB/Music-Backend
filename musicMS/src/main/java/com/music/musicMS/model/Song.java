package com.music.musicMS.model;

import java.sql.Date;
import java.util.ArrayList;
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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Song {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private Date releaseDate;
	
	@Column(nullable = false)
	private Integer reproductions;
	
	@Column(nullable = false)
	private Integer duration;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Album album;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			joinColumns = {@JoinColumn(name = "song_id")},
			inverseJoinColumns = {@JoinColumn(name = "artist_id")})
	private List<Artist> artists = new ArrayList<>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			joinColumns = {@JoinColumn(name = "song_id")},
			inverseJoinColumns = {@JoinColumn(name = "genre_id")})
	private List<Genre> genres = new ArrayList<>();
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "songs")
	private List<Playlist> playlists;
	
	public Song(SongRequestDTO request, Album album) {
		this.name = request.getName();
		this.reproductions = 0;
		this.duration = request.getDuration();
		this.album = album;
	}
}

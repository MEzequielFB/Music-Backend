package com.music.backend.model;

import java.util.List;

import com.music.backend.dto.PlaylistRequestDTO;

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
public class Playlist {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private boolean isPublic;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			joinColumns = {@JoinColumn(name = "playlist_id")},
			inverseJoinColumns = {@JoinColumn(name = "song_id")})
	private List<Song> songs;
	
	public Playlist(PlaylistRequestDTO request) {
		this.name = request.getName();
		this.isPublic = request.isPublic();
		this.user = request.getUser();
	}
	
	public void addSong(Song song) {
		songs.add(song);
	}
}

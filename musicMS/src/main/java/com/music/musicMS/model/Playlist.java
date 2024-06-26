package com.music.musicMS.model;

import java.util.Set;

import com.music.musicMS.dto.PlaylistRequestDTO;

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
public class Playlist {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private Boolean isPublic;
	
	@Column(nullable = false)
	private Integer userId;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			joinColumns = {@JoinColumn(name = "playlist_id")},
			inverseJoinColumns = {@JoinColumn(name = "song_id")})
	private Set<Song> songs;
	
	public Playlist(PlaylistRequestDTO request, Integer userId) {
		this.name = request.getName();
		this.isPublic = request.getIsPublic();
		this.userId = userId;
	}
	
	public void addSong(Song song) {
		songs.add(song);
	}
	
	public void removeSong(Song song) {
		songs.remove(song);
	}
}

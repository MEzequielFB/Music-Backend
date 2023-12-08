package com.music.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.music.backend.model.Playlist;
import com.music.backend.model.Song;
import com.music.backend.model.User;

@Repository(value = "playlistRepository")
public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {

	@Query("SELECT p"
			+ " FROM Playlist p"
			+ " WHERE p.user = :user"
			+ " AND p.name = :name")
	public Optional<Playlist> findByUserAndName(User user, String name);
	
//	@Query("SELECT"
//			+ " CASE WHEN :song IN p.songs THEN true"
//			+ " ELSE false"
//			+ " END AS contains_song"
//			+ " FROM Playlist"
//			+ " JOIN p.songs s"
//			+ " WHERE id = :id")
//	public boolean containsSong(int id, Song song);
}

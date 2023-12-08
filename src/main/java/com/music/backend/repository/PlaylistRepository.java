package com.music.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.music.backend.model.Playlist;
import com.music.backend.model.User;

@Repository(value = "playlistRepository")
public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {

	@Query("SELECT p"
			+ " FROM Playlist p"
			+ " WHERE p.user = :user"
			+ " AND p.name = :name")
	public Optional<Playlist> findByUserAndName(User user, String name);
}

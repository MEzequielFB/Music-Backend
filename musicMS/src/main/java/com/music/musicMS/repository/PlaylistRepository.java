package com.music.musicMS.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.music.musicMS.dto.SongResponseDTO;
import com.music.musicMS.model.Playlist;

@Repository(value = "playlistRepository")
public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {

	@Query("SELECT p"
			+ " FROM Playlist p"
			+ " WHERE p.user.id = :userId"
			+ " AND p.name = :name")
	public Optional<Playlist> findByUserAndName(Integer userId, String name);

	@Query("SELECT new com.music.backend.dto.SongResponseDTO(s)"
			+ " FROM Playlist p"
			+ " JOIN p.songs s"
			+ " WHERE p.id = :id")
	public List<SongResponseDTO> getSongsFromPlaylist(Integer id);
}

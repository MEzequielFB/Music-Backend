package com.music.musicMS.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.music.musicMS.dto.SongResponseDTO;
import com.music.musicMS.model.Playlist;
import com.music.musicMS.model.Song;

@Repository(value = "playlistRepository")
public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
	
	@Query("SELECT p"
			+ " FROM Playlist p"
			+ " WHERE p.isPublic = true")
	public List<Playlist> findAllByPublic();
	
	@Query("SELECT p"
			+ " FROM Playlist p"
			+ " WHERE :song IN elements(p.songs)")
	public List<Playlist> findAllBySong(Song song);

	@Query("SELECT p"
			+ " FROM Playlist p"
			+ " WHERE p.userId = :userId"
			+ " AND p.name = :name")
	public Optional<Playlist> findByUserAndName(Integer userId, String name);
	
	@Query("SELECT p"
			+ " FROM Playlist p"
			+ " WHERE p.userId = :userId")
	public List<Playlist> findAllByUserId(Integer userId);

	@Query("SELECT new com.music.musicMS.dto.SongResponseDTO(s)"
			+ " FROM Playlist p"
			+ " JOIN p.songs s"
			+ " WHERE p = :playlist")
	public List<SongResponseDTO> getSongsFromPlaylist(Playlist playlist);
}

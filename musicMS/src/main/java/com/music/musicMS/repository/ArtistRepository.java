package com.music.musicMS.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.music.musicMS.dto.ArtistResponseDTO;
import com.music.musicMS.model.Artist;

@Repository(value = "artistRepository")
public interface ArtistRepository extends JpaRepository<Artist, Integer> {
	
	@Query("SELECT a"
			+ " FROM Artist a"
			+ " WHERE a.name LIKE CONCAT(:name, '%')"
			+ " AND a.isDeleted = false")
	public List<ArtistResponseDTO> findAllByName(String name);
	
	@Query("SELECT a"
			+ " FROM Artist a"
			+ " WHERE a.isDeleted = false")
	public List<Artist> findAllNotDeleted();
	
	@Query("SELECT a"
			+ " FROM Artist a"
			+ " WHERE a.isDeleted = true")
	public List<Artist> findAllDeleted();
	
	@Query("SELECT a"
			+ " FROM Artist a"
			+ " WHERE a.userId = :userId")
	public Optional<Artist> findByUserId(Integer userId);
	
	@Query("SELECT a"
			+ " FROM Artist a"
			+ " WHERE a.id IN :ids"
			+ " AND a.isDeleted = false")
	public List<Artist> findAllByIds(List<Integer> ids);
	
	@Query("SELECT new com.music.musicMS.dto.ArtistResponseDTO(a)"
			+ " FROM Artist a"
			+ " WHERE a.id IN :ids")
	public List<ArtistResponseDTO> findAllByIdDTO(List<Integer> ids);

	@Query("SELECT a"
			+ " FROM Artist a"
			+ " WHERE a.name = :name")
	public Optional<Artist> findByName(String name);
}

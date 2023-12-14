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
			+ " WHERE a.id IN :ids")
	public List<Artist> findAllById(List<Integer> ids);
	
	@Query("SELECT new com.music.musicMS.dto.ArtistResponseDTO(a)"
			+ " FROM Artist a"
			+ " WHERE a.id IN :ids")
	public List<ArtistResponseDTO> findAllByIdDTO(List<Integer> ids);

	@Query("SELECT a"
			+ " FROM Artist a"
			+ " WHERE a.name = :name")
	public Optional<Artist> findByName(String name);
}

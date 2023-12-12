package com.music.userMS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.music.userMS.model.Artist;

@Repository(value = "artistRepository")
public interface ArtistRepository extends JpaRepository<Artist, Integer> {
	
	@Query("SELECT a"
			+ " FROM Artist a"
			+ " WHERE a.id IN :ids")
	public List<Artist> findAllById(List<Integer> ids);
}

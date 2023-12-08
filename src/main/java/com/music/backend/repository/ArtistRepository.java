package com.music.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.music.backend.model.Artist;

@Repository(value = "artistRepository")
public interface ArtistRepository extends JpaRepository<Artist, Integer> {
	
}

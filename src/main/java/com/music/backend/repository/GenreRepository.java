package com.music.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.music.backend.model.Genre;

@Repository(value = "genreRepository")
public interface GenreRepository extends JpaRepository<Genre, Integer> {
	
	@Query("SELECT g"
			+ " FROM Genre g"
			+ " WHERE g.name = :name")
	public Optional<Genre> findByName(String name);
}

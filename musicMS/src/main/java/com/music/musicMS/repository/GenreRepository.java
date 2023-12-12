package com.music.musicMS.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.music.musicMS.model.Genre;

@Repository(value = "genreRepository")
public interface GenreRepository extends JpaRepository<Genre, Integer> {
	
	@Query("SELECT g"
			+ " FROM Genre g"
			+ " WHERE g.name = :name")
	public Optional<Genre> findByName(String name);
	
	@Query("SELECT g"
			+ " FROM Genre g"
			+ " WHERE g.id IN :ids")
	public List<Genre> findAllById(List<Integer> ids);
}

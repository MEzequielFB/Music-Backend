package com.music.musicMS.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.music.musicMS.model.Album;
import com.music.musicMS.model.Artist;

@Repository(value = "albumRepository")
public interface AlbumRepository extends JpaRepository<Album, Integer> {

	@Query("SELECT a"
			+ " FROM Album a"
			+ " WHERE a.name = :name"
			+ " AND a.owner = :owner")
	public Optional<Album> findByNameAndOwner(String name, Artist owner);
	
	@Query("SELECT a"
			+ " FROM Album a"
			+ " WHERE a.owner = :owner")
	public List<Album> findAllByOwner(Artist owner);
}

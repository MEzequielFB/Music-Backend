package com.music.musicMS.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.music.musicMS.model.Playlist;
import com.music.musicMS.model.Song;

@Repository(value = "songRepository")
public interface SongRepository extends JpaRepository<Song, Integer> {
	
	@Query("SELECT s"
			+ " FROM Song s"
			+ " JOIN s.playlists p"
			+ " WHERE s.id = :id"
			+ " AND p = :playlist")
	public Optional<Song> findByPlaylist(int id, Playlist playlist);
	
	@Query("SELECT s"
			+ " FROM Song s"
			+ " WHERE YEAR(s.releaseDate) IN :years")
	public List<Song> findByYear(List<Integer> years);
	
	@Query("SELECT s"
			+ " FROM Song s"
			+ " JOIN s.genres g"
			+ " WHERE g.name IN :genres")
	public List<Song> findByGenre(List<String> genres);
	
	@Query("SELECT s"
			+ " FROM Song s"
			+ " JOIN s.genres g"
			+ " WHERE g.name IN :genres"
			+ " AND YEAR(s.releaseDate) IN :years")
	public List<Song> findByGenreAndYear(List<String> genres, List<Integer> years);
	
	@Query("SELECT s"
			+ " FROM Song s"
			+ " JOIN s.genres g"
			+ " WHERE s.name LIKE :name%"
			+ " AND g.name IN :genres"
			+ " AND YEAR(s.releaseDate) IN :years")
	public List<Song> findByNameGenreAndYear(String name, List<String> genres, List<Integer> years);
	
	@Query("SELECT s"
			+ " FROM Song s"
			+ " WHERE s.name LIKE :name%"
			+ " AND YEAR(s.releaseDate) IN :years")
	public List<Song> findByNameAndYear(String name, List<Integer> years);
	
	@Query("SELECT s"
			+ " FROM Song s"
			+ " JOIN s.genres g"
			+ " WHERE s.name LIKE :name%"
			+ " AND g.name IN :genres")
	public List<Song> findByNameAndGenre(String name, List<String> genres);
	
	@Query("SELECT s"
			+ " FROM Song s"
			+ " WHERE s.name LIKE :name%")
	public List<Song> findByName(String name);

	@Query("SELECT s"
			+ " FROM Song s"
			+ " WHERE s.name = :name"
			+ " AND s.artists = :artists")
	public Optional<Song> findByArtistsAndName(List<Integer> artists, String name);
}

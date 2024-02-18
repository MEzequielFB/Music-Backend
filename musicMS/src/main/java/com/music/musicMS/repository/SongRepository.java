package com.music.musicMS.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.music.musicMS.model.Album;
import com.music.musicMS.model.Artist;
import com.music.musicMS.model.Playlist;
import com.music.musicMS.model.Song;

@Repository(value = "songRepository")
public interface SongRepository extends JpaRepository<Song, Integer> {
	
	@Query("SELECT s"
			+ " FROM Song s"
			+ " JOIN s.genres g"
			+ " WHERE s.name IN :data"
	        + " OR CAST(YEAR(s.releaseDate) AS string) IN :data"
			+ " OR g.name IN :data")
	public List<Song> findAllByFilters(List<String> data);
	
	@Query("SELECT s"
			+ " FROM Song s"
			+ " JOIN s.playlists p"
			+ " WHERE s.id = :id"
			+ " AND p = :playlist")
	public Optional<Song> findByPlaylist(Integer id, Playlist playlist);
	
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
			+ " JOIN s.artists a"
			+ " WHERE s.name = :name"
			+ " AND a.id IN :artists")
	public Optional<Song> findByArtistsAndName(List<Integer> artists, String name);
	
	@Query("SELECT DISTINCT s.artists"
			+ " FROM Song s"
			+ " WHERE s IN :songs")
	public List<Artist> findArtistsBySongs(List<Song> songs);
	
	@Modifying
	@Query("UPDATE Song s"
			+ " SET s.album = null"
			+ " WHERE s.album = album")
	public void removeSongsFromAlbum(Album album);
	
	@Query("SELECT"
			+ " CASE"
				+ " WHEN :artist MEMBER OF s.artists THEN TRUE"
				+ " ELSE FALSE"
			+ " END"
			+ " FROM Song s"
			+ " WHERE s = :song")
	public Boolean songContainsArtist(Song song, Artist artist);
}

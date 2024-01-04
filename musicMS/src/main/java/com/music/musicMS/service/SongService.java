package com.music.musicMS.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.music.musicMS.dto.SongRequestDTO;
import com.music.musicMS.dto.SongResponseDTO;
import com.music.musicMS.exception.NameAlreadyUsedException;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.exception.SomeEntityDoesNotExistException;
import com.music.musicMS.model.Album;
import com.music.musicMS.model.Artist;
import com.music.musicMS.model.Genre;
import com.music.musicMS.model.Song;
import com.music.musicMS.repository.AlbumRepository;
import com.music.musicMS.repository.ArtistRepository;
import com.music.musicMS.repository.GenreRepository;
import com.music.musicMS.repository.SongRepository;

@Service(value = "songService")
public class SongService {

	@Autowired
	private SongRepository repository;
	
	@Autowired
	private GenreRepository genreRepository;
	
	@Autowired
	private ArtistRepository artistRepository;
	
	@Autowired
	private AlbumRepository albumRepository;
	
	@Transactional(readOnly = true)
	public List<SongResponseDTO> searchSongs(String name, List<String> genres, List<Integer> years) {
		if (name != null) {
			if (genres == null && years == null) {
				return repository.findByName(name)
						.stream()
						.map( SongResponseDTO::new ).toList();
			} else if (genres != null && years == null) {
				return repository.findByNameAndGenre(name, genres)
						.stream()
						.map( SongResponseDTO::new ).toList();
			} else if (genres == null && years != null) {
				return repository.findByNameAndYear(name, years)
						.stream()
						.map( SongResponseDTO::new ).toList();
			} else {
				return repository.findByNameGenreAndYear(name, genres, years)
						.stream()
						.map( SongResponseDTO::new ).toList();
			}
		} else {
			if (genres != null && years == null) {
				return repository.findByGenre(genres)
						.stream()
						.map( SongResponseDTO::new ).toList();
			} else if (genres == null && years != null) {
				return repository.findByYear(years)
						.stream()
						.map( SongResponseDTO::new ).toList();
			} else {
				return repository.findByGenreAndYear(genres, years)
						.stream()
						.map( SongResponseDTO::new ).toList();
			}
		}
	}
	
	@Transactional(readOnly = true)
	public List<SongResponseDTO> findAll() {
		return repository.findAll()
				.stream()
				.map( SongResponseDTO::new ).toList();
	}
	
	@Transactional(readOnly = true)
	public SongResponseDTO findById(Integer id) throws NotFoundException {
		Optional<Song> optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new NotFoundException("Song", id);
		}
		Song song = optional.get();
		
		return new SongResponseDTO(song); 
	}
	
	@Transactional
	public SongResponseDTO saveSong(SongRequestDTO request) throws NameAlreadyUsedException, SomeEntityDoesNotExistException, NotFoundException {
		Optional<Song> optional = repository.findByArtistsAndName(request.getArtists(), request.getName());
		Optional<Album> albumOptional = null;
		Album album = null;
		
		if (request.getAlbumId() != null) {
			albumOptional = albumRepository.findById(request.getAlbumId());
			
			if (!albumOptional.isPresent()) {
				throw new NotFoundException("Album", request.getAlbumId());
			}
			
			album = albumOptional.get();
		}
		
		if (optional.isPresent()) {
			throw new NameAlreadyUsedException("Song", request.getName());
		}
		
		List<Artist> artists = artistRepository.findAllById(request.getArtists());
		
		if (artists.size() != request.getArtists().size()) {
			throw new SomeEntityDoesNotExistException("artists");
		}
		
		List<Genre> genres = genreRepository.findAllById(request.getGenres());
		
		if (genres.size() != request.getGenres().size()) {
			throw new SomeEntityDoesNotExistException("genres");
		}
		
		Song song = new Song(request, album);
		song.setGenres(genres);
		song.setArtists(artists);
		
		//Default date -> current date
		song.setReleaseDate(new Date(System.currentTimeMillis()));
		
		SongResponseDTO responseDTO = new SongResponseDTO(repository.save(song));
		
		return responseDTO;
	}
	
	@Transactional
	public SongResponseDTO updateSong(int id, SongRequestDTO request) throws SomeEntityDoesNotExistException, NotFoundException {
		Optional<Song> optional = repository.findById(id);
		Optional<Album> albumOptional = albumRepository.findById(request.getAlbumId());
		
		if (!optional.isPresent()) {
			throw new NotFoundException("Song", id);
		}
		if (!albumOptional.isPresent()) {
			throw new NotFoundException("Album", request.getAlbumId());
		}

		Song song = optional.get();
		Album album = albumOptional.get();
		song.setName(request.getName());
		song.setAlbum(album);
		
		List<Artist> artists = artistRepository.findAllById(request.getArtists());
		
		if (artists.size() != request.getArtists().size()) {
			throw new SomeEntityDoesNotExistException("artists");
		}
		
		song.setArtists(artists);
		
		List<Genre> genres = genreRepository.findAllById(request.getGenres());
		
		if (genres.size() != request.getGenres().size()) {
			throw new SomeEntityDoesNotExistException("genres");
		}
		song.setGenres(genres);
		
		SongResponseDTO responseDTO = new SongResponseDTO(repository.save(song));
		
		return responseDTO;
	}
	
	@Transactional
	public SongResponseDTO deleteSong(int id) throws NotFoundException {
		Optional<Song> optional = repository.findById(id);
		if (optional.isPresent()) {
			Song song = optional.get();
			repository.deleteById(id);
			
			SongResponseDTO responseDTO = new SongResponseDTO(song);
			
			return responseDTO;
		} else {
			throw new NotFoundException("Song", id);
		}
	}
}

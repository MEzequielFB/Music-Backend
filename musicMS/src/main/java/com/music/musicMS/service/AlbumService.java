package com.music.musicMS.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.music.musicMS.dto.AlbumRequestDTO;
import com.music.musicMS.dto.AlbumResponseDTO;
import com.music.musicMS.dto.AlbumUpdateDTO;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.exception.SomeEntityDoesNotExistException;
import com.music.musicMS.model.Album;
import com.music.musicMS.model.Artist;
import com.music.musicMS.model.Song;
import com.music.musicMS.repository.AlbumRepository;
import com.music.musicMS.repository.ArtistRepository;
import com.music.musicMS.repository.SongRepository;

@Service(value = "albumService")
public class AlbumService {

	@Autowired
	private AlbumRepository repository;
	
	@Autowired
	private ArtistRepository artistRepository;
	
	@Autowired
	private SongRepository songRepository;
	
	@Transactional(readOnly = true)
	public List<AlbumResponseDTO> findAll() {
		return repository.findAll().stream().map( AlbumResponseDTO::new ).toList();
	}
	
	@Transactional(readOnly = true)
	public AlbumResponseDTO findById(int id) throws NotFoundException {
		Optional<Album> optional = repository.findById(id);
		if (optional.isPresent()) {
			Album album = optional.get();
			return new AlbumResponseDTO(album);
		} else {
			throw new NotFoundException("Album", id);
		}
	}
	
	@Transactional
	public AlbumResponseDTO saveAlbum(AlbumRequestDTO request) {
		Album album = new Album(request);
		
		return new AlbumResponseDTO(repository.save(album));
	}
	
	@Transactional
	public AlbumResponseDTO updateAlbum(int id, AlbumUpdateDTO request) throws NotFoundException, SomeEntityDoesNotExistException {
		Optional<Album> optional = repository.findById(id);
		if (optional.isPresent()) {
			Album album = optional.get();
			album.setName(request.getName());
			
//			List<Song> songs = songRepository.findAllById(request.getSongs());
//			if (songs.size() != request.getSongs().size()) {
//				throw new SomeEntityDoesNotExistException("songs");
//			}
//			
//			for (Song song : songs) {
//				song.setAlbum(album);
//			}
//			
//			List<Artist> artists = songRepository.findSongsArtists(songs);
//			for (Artist artist : artists) {
//				artist.addAlbum(album);
//			}
//			
//			// Set the songs and artists for the DTO response
//			album.setSongs(songs);
//			album.setArtists(artists);
			
			return new AlbumResponseDTO(repository.save(album));
		} else {
			throw new NotFoundException("Album", id);
		}
	}
	
	@Transactional
	public AlbumResponseDTO deleteAlbum(int id) throws NotFoundException {
		Optional<Album> optional = repository.findById(id);
		if (optional.isPresent()) {
			Album album = optional.get();
			
			songRepository.setAlbumNullOfSongs(album);
			for (Artist artist : album.getArtists()) {
				artist.removeAlbum(album);
			}
			
			repository.deleteById(id);
			
			return new AlbumResponseDTO(album);
		} else {
			throw new NotFoundException("Album", id);
		}
	}
}

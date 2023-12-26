package com.music.musicMS.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.music.musicMS.dto.AlbumRequestDTO;
import com.music.musicMS.dto.AlbumResponseDTO;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.exception.SomeEntityDoesNotExistException;
import com.music.musicMS.model.Album;
import com.music.musicMS.model.Artist;
import com.music.musicMS.repository.AlbumRepository;
import com.music.musicMS.repository.ArtistRepository;

@Service(value = "albumService")
public class AlbumService {

	@Autowired
	private AlbumRepository repository;
	
	@Autowired
	private ArtistRepository artistRepository;
	
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
	public AlbumResponseDTO saveAlbum(AlbumRequestDTO request) throws SomeEntityDoesNotExistException {
		Album album = new Album(request);
		List<Artist> artists = artistRepository.findAllById(request.getArtists());
		if (artists.size() != request.getArtists().size()) {
			throw new SomeEntityDoesNotExistException("artists");
		}
		
		album.setArtists(artists);
		
		return new AlbumResponseDTO(repository.save(album));
	}
	
	@Transactional
	public AlbumResponseDTO updateAlbum(int id, AlbumRequestDTO request) throws NotFoundException, SomeEntityDoesNotExistException {
		Optional<Album> optional = repository.findById(id);
		if (optional.isPresent()) {
			Album album = optional.get();
			album.setName(request.getName());
			album.setSongs(request.getSongs());
			
			List<Artist> artists = artistRepository.findAllById(request.getArtists());
			if (artists.size() != request.getArtists().size()) {
				throw new SomeEntityDoesNotExistException("artists");
			}
			
			album.setArtists(artists);
			
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
			repository.deleteById(id);
			
			return new AlbumResponseDTO(album);
		} else {
			throw new NotFoundException("Album", id);
		}
	}
}

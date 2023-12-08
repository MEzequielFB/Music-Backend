package com.music.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.music.backend.dto.AlbumRequestDTO;
import com.music.backend.dto.AlbumResponseDTO;
import com.music.backend.exception.NotFoundException;
import com.music.backend.model.Album;
import com.music.backend.repository.AlbumRepository;

@Service(value = "albumService")
public class AlbumService {

	@Autowired
	private AlbumRepository repository;
	
	@Transactional(readOnly = true)
	public List<AlbumResponseDTO> findAll() {
		return repository.findAll().stream().map( AlbumResponseDTO::new ).toList();
	}
	
	@Transactional(readOnly = true)
	public AlbumResponseDTO findById(int id) throws NotFoundException {
		Optional<Album> optional = repository.findById(id);
		if (optional.isPresent()) {
			return new AlbumResponseDTO(optional.get());
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
	public AlbumResponseDTO updateAlbum(int id, AlbumRequestDTO request) throws NotFoundException {
		Optional<Album> optional = repository.findById(id);
		if (optional.isPresent()) {
			Album album = optional.get();
			album.setName(request.getName());
			album.setArtists(request.getArtists());
			album.setSongs(request.getSongs());
			
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

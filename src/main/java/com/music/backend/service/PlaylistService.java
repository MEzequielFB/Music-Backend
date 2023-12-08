package com.music.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.music.backend.dto.PlaylistRequestDTO;
import com.music.backend.dto.PlaylistResponseDTO;
import com.music.backend.exception.NameAlreadyUsedException;
import com.music.backend.exception.NotFoundException;
import com.music.backend.model.Playlist;
import com.music.backend.repository.PlaylistRepository;

import jakarta.transaction.Transactional;

@Service(value = "playlistService")
public class PlaylistService {

	@Autowired
	private PlaylistRepository repository;
	
	@Transactional
	public List<PlaylistResponseDTO> findAll() {
		return repository.findAll().stream().map( PlaylistResponseDTO::new ).toList();
	}
	
	@Transactional
	public PlaylistResponseDTO findById(int id) throws NotFoundException {
		Optional<Playlist> optional = repository.findById(id);
		if (optional.isPresent()) {
			return new PlaylistResponseDTO(optional.get());
		} else {
			throw new NotFoundException("Playlist", id);
		}
	}
	
	@Transactional
	public PlaylistResponseDTO savePlaylist(PlaylistRequestDTO request) throws NameAlreadyUsedException {
		//Doesn't allow playlists with the same name from the same user 
		Optional<Playlist> optional = repository.findByUserAndName(request.getUser(), request.getName());
		if (!optional.isPresent()) {
			Playlist playlist = new Playlist(request);
			return new PlaylistResponseDTO(repository.save(playlist));
		} else {
			throw new NameAlreadyUsedException("Playlist", request.getName());
		}
	}
	
	@Transactional
	public PlaylistResponseDTO updatePlaylist(int id, PlaylistRequestDTO request) throws NotFoundException {
		Optional<Playlist> optional = repository.findById(id);
		if (optional.isPresent()) {
			Playlist playlist = optional.get();
			playlist.setName(request.getName());
			playlist.setPublic(request.isPublic());
			
			return new PlaylistResponseDTO(repository.save(playlist));
		} else {
			throw new NotFoundException("Playlist", id);
		}
	}
	
	@Transactional
	public PlaylistResponseDTO deletePlaylist(int id) throws NotFoundException {
		Optional<Playlist> optional = repository.findById(id);
		if (optional.isPresent()) {
			Playlist playlist = optional.get();
			repository.deleteById(id);
			
			return new PlaylistResponseDTO(playlist);
		} else {
			throw new NotFoundException("Playlist", id);
		}
	}
}

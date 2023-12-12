package com.music.musicMS.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.music.musicMS.dto.AddSongRequestDTO;
import com.music.musicMS.dto.PlaylistRequestDTO;
import com.music.musicMS.dto.PlaylistResponseDTO;
import com.music.musicMS.dto.PlaylistUpdateDTO;
import com.music.musicMS.dto.SongResponseDTO;
import com.music.musicMS.exception.AlreadyContainsSongException;
import com.music.musicMS.exception.NameAlreadyUsedException;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.model.Playlist;
import com.music.musicMS.model.Song;
import com.music.musicMS.repository.PlaylistRepository;

@Service(value = "playlistService")
public class PlaylistService {

	@Autowired
	private PlaylistRepository repository;
	
	@Autowired
	private SongRepository songRepository;
	
	@Transactional(readOnly = true)
	public List<PlaylistResponseDTO> findAll() {
		return repository.findAll().stream().map( PlaylistResponseDTO::new ).toList();
	}
	
	@Transactional(readOnly = true)
	public PlaylistResponseDTO findById(int id) throws NotFoundException {
		Optional<Playlist> optional = repository.findById(id);
		if (optional.isPresent()) {
			return new PlaylistResponseDTO(optional.get());
		} else {
			throw new NotFoundException("Playlist", id);
		}
	}
	
	public List<SongResponseDTO> getSongsFromPlaylist(int id) throws NotFoundException {
		Optional<Playlist> optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new NotFoundException("Playlist", id);
		}
		
		return repository.getSongsFromPlaylist(id);
	}
	
	@Transactional
	public PlaylistResponseDTO savePlaylist(PlaylistRequestDTO request) throws NameAlreadyUsedException, NotFoundException {
		Optional<User> userOptional = userRepository.findById(request.getUser().getId());
		if (!userOptional.isPresent()) {
			throw new NotFoundException("User", request.getUser().getId());
		}
		
		//Doesn't allow playlists with the same name from the same user
		Optional<Playlist> optional = repository.findByUserAndName(request.getUser(), request.getName());
		if (optional.isPresent()) {
			throw new NameAlreadyUsedException("Playlist", request.getName());
		}
		
		Playlist playlist = new Playlist(request);
		User user = userOptional.get();
		
		playlist.setUser(user);
		return new PlaylistResponseDTO(repository.save(playlist));
	}
	
	public SongResponseDTO addSong(int id, AddSongRequestDTO request) throws NotFoundException, AlreadyContainsSongException {
		Optional<Playlist> optional = repository.findById(id);
		Optional<Song> songOptional = songRepository.findById(request.getSongId());
		if (!optional.isPresent()) {
			throw new NotFoundException("Playlist", id);
		}
		if (!songOptional.isPresent()) {
			throw new NotFoundException("Song", request.getSongId());
		}
		
		Playlist playlist = optional.get();
		Song song = songOptional.get();
		
//		boolean containsSong = repository.containsSong(id, song);
//		if (containsSong) {
//			throw new AlreadyContainsSongException("Playlist");
//		}
		Optional<Song> songOptional2 = songRepository.findByPlaylist(request.getSongId(), playlist);
		if (songOptional2.isPresent()) {
			throw new AlreadyContainsSongException("Playlist");
		}
		
		playlist.addSong(song);
		repository.save(playlist);
		
		return new SongResponseDTO(song);
	}
	
	@Transactional
	public PlaylistResponseDTO updatePlaylist(int id, PlaylistUpdateDTO request) throws NotFoundException {
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

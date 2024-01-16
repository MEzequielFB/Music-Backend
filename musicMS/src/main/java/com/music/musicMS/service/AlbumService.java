package com.music.musicMS.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.music.musicMS.dto.AlbumRequestDTO;
import com.music.musicMS.dto.AlbumResponseDTO;
import com.music.musicMS.dto.AlbumUpdateDTO;
import com.music.musicMS.exception.AlbumOwnerNotInSongException;
import com.music.musicMS.exception.DoNotContainsTheSongException;
import com.music.musicMS.exception.NameAlreadyUsedException;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.exception.SomeEntityDoesNotExistException;
import com.music.musicMS.exception.SongIsAlreadyInAnAlbumException;
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
	
	@Transactional(readOnly = true)
	public List<AlbumResponseDTO> findAllByOwner(Integer ownerId) throws NotFoundException {
		Optional<Artist> artistOptional = artistRepository.findById(ownerId);
		if (artistOptional.isPresent()) {
			Artist artist = artistOptional.get();
			return artist.getOwnedAlbums()
					.stream()
					.map( AlbumResponseDTO::new ).toList();
		} else {
			throw new NotFoundException("Artist", ownerId);
		}
	}
	
	@Transactional
	public AlbumResponseDTO saveAlbum(AlbumRequestDTO request) throws NotFoundException, NameAlreadyUsedException {
		Optional<Artist> artistOptional = artistRepository.findById(request.getOwnerId());
		if (!artistOptional.isPresent()) {
			throw new NotFoundException("Artist", request.getOwnerId());
		}
		
		Artist owner = artistOptional.get();
		
		Optional<Album> albumOptional = repository.findByNameAndOwner(request.getName(), owner);
		if (albumOptional.isPresent()) {
			throw new NameAlreadyUsedException("Album", request.getName());
		}
		
		Album album = new Album(request);
		album.setOwner(owner);
		
		return new AlbumResponseDTO(repository.save(album));
	}
	
	@Transactional
	public AlbumResponseDTO updateAlbum(int id, AlbumUpdateDTO request) throws NotFoundException, SomeEntityDoesNotExistException {
		Optional<Album> optional = repository.findById(id);
		if (optional.isPresent()) {
			Album album = optional.get();
			album.setName(request.getName());
			
			return new AlbumResponseDTO(repository.save(album));
		} else {
			throw new NotFoundException("Album", id);
		}
	}
	
	@Transactional
	public AlbumResponseDTO addSong(Integer id, Integer songId) throws NotFoundException, SongIsAlreadyInAnAlbumException, AlbumOwnerNotInSongException {
		Optional<Album> optional = repository.findById(id);
		Optional<Song> songOptional = songRepository.findById(songId);
		
		if (!optional.isPresent()) {
			throw new NotFoundException("Album", id);
		}
		if (!songOptional.isPresent()) {
			throw new NotFoundException("Song", songId);
		}
		
		Album album = optional.get();
		Song song = songOptional.get();
	
		if (song.getAlbum() != null) {
			throw new SongIsAlreadyInAnAlbumException(song.getName());
		}
		if (!songRepository.songContainsArtist(song, album.getOwner())) {
			throw new AlbumOwnerNotInSongException(song.getName(), album.getOwner().getName());
		}
		
		song.setAlbum(album);
		song = songRepository.save(song);
		
		album.addSong(song);
		
		List<Artist> artists = songRepository.findArtistsBySongs(album.getSongs());
		album.setArtists(artists);
		
		return new AlbumResponseDTO(repository.save(album));
	}
	
	@Transactional
	public AlbumResponseDTO removeSong(Integer id, Integer songId) throws NotFoundException, DoNotContainsTheSongException {
		Optional<Album> optional = repository.findById(id);
		Optional<Song> songOptional = songRepository.findById(songId);
		
		if (!optional.isPresent()) {
			throw new NotFoundException("Album", id);
		}
		if (!songOptional.isPresent()) {
			throw new NotFoundException("Song", songId);
		}
		
		Album album = optional.get();
		Song song = songOptional.get();
		
		if (song.getAlbum() != null && song.getAlbum().equals(album)) {
			song.setAlbum(null);
			album.removeSong(song);
		} else {
			throw new DoNotContainsTheSongException(album.getName(), song.getName());
		}

		List<Artist> artists = songRepository.findArtistsBySongs(album.getSongs());
		album.setArtists(artists);
		
		return new AlbumResponseDTO(repository.save(album));
	}
	
	@Transactional
	public AlbumResponseDTO deleteAlbum(int id) throws NotFoundException {
		Optional<Album> optional = repository.findById(id);
		if (optional.isPresent()) {
			Album album = optional.get();
			album.getArtists().clear();
			
			repository.deleteById(id);
			
			return new AlbumResponseDTO(album);
		} else {
			throw new NotFoundException("Album", id);
		}
	}
}

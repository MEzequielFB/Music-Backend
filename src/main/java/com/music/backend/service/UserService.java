package com.music.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.music.backend.dto.UserRequestDTO;
import com.music.backend.dto.UserResponseDTO;
import com.music.backend.exception.EmailAlreadyUsedException;
import com.music.backend.exception.NotFoundException;
import com.music.backend.model.User;
import com.music.backend.repository.UserRepository;

@Service(value = "userService")
public class UserService {
	
	@Autowired
	private UserRepository repository;
	
	@Transactional(readOnly = true)
	public List<UserResponseDTO> findFollowedUsersById(int id) throws NotFoundException {
		Optional<User> optional = repository.findById(id);
		if (optional.isPresent()) {
			return repository.findFollowedUsersById(id).stream().map( UserResponseDTO::new ).toList();
		} else {
			throw new NotFoundException("User", id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<UserResponseDTO> findFollowersById(int id) throws NotFoundException {
		Optional<User> optional = repository.findById(id);
		if (optional.isPresent()) {
			return repository.findFollowersById(id).stream().map( UserResponseDTO::new ).toList();
		} else {
			throw new NotFoundException("User", id);
		}
	}

	@Transactional(readOnly = true)
	public List<UserResponseDTO> findAll() {
		return repository.findAll().stream().map( UserResponseDTO::new ).toList();
	}
	
	@Transactional(readOnly = true)
	public UserResponseDTO findById(int id) throws NotFoundException {
		Optional<User> optional = repository.findById(id);
		if (optional.isPresent()) {
			return new UserResponseDTO(optional.get());
		} else {
			throw new NotFoundException("User", id);
		}
	}
	
	@Transactional
	public UserResponseDTO saveUser(UserRequestDTO request) throws EmailAlreadyUsedException {
		Optional<User> optional = repository.findByEmail(request.getEmail());
		if (!optional.isPresent()) {
			User user = new User(request);
			//default value
			user.setRole(0);
			
			return new UserResponseDTO(repository.save(user));
		} else {
			throw new EmailAlreadyUsedException(request.getEmail());
		}
	}
	
	@Transactional
	public UserResponseDTO updateUser(int id, UserRequestDTO request) throws NotFoundException {
		Optional<User> optional = repository.findById(id);
		if (optional.isPresent()) {
			User user = optional.get();
			user.setUsername(request.getUsername());
			user.setEmail(request.getEmail());
			user.setPassword(request.getPassword());
			
			return new UserResponseDTO(repository.save(user));
		} else {
			throw new NotFoundException("User", id);
		}
	}
	
	@Transactional
	public UserResponseDTO deleteUser(int id) throws NotFoundException {
		Optional<User> optional = repository.findById(id);
		if (optional.isPresent()) {
			User user = optional.get();
			repository.deleteById(id);
			
			return new UserResponseDTO(user);
		} else {
			throw new NotFoundException("User", id);
		}
	}
}

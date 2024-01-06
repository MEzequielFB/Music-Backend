package com.music.userMS.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.music.userMS.dto.RoleRequestDTO;
import com.music.userMS.dto.RoleResponseDTO;
import com.music.userMS.exception.NameAlreadyUsedException;
import com.music.userMS.exception.NotFoundException;
import com.music.userMS.model.Role;
import com.music.userMS.repository.RoleRepository;

@Service(value = "roleService")
public class RoleService {
	
	@Autowired
	private RoleRepository repository;

	@Transactional(readOnly = true)
	public List<RoleResponseDTO> findAll() {
		return repository.findAll()
				.stream()
				.map( RoleResponseDTO::new ).toList();
	}

	@Transactional(readOnly = true)
	public RoleResponseDTO findById(Integer id) throws NotFoundException {
		Optional<Role> optional = repository.findById(id);
		if (optional.isPresent()) {
			return new RoleResponseDTO(optional.get());
		} else {
			throw new NotFoundException("Role", id);
		}
	}

	@Transactional
	public RoleResponseDTO saveRole(RoleRequestDTO request) throws NameAlreadyUsedException {
		Optional<Role> optional = repository.findByName(request.getName());
		if (!optional.isPresent()) {
			Role role = new Role(request);
			return new RoleResponseDTO(repository.save(role));
		} else {
			throw new NameAlreadyUsedException("role", request.getName());
		}
	}
	
	@Transactional
	public RoleResponseDTO updateRole(Integer id, RoleRequestDTO request) throws NameAlreadyUsedException, NotFoundException {
		Optional<Role> optional = repository.findById(id);
		Optional<Role> roleWithNameOptional = repository.findByName(request.getName());
		
		if (!optional.isPresent()) {
			throw new NotFoundException("Role", id);
		}
		if (roleWithNameOptional.isPresent()) {
			throw new NameAlreadyUsedException("role", request.getName());
		}
		
		Role role = optional.get();
		return new RoleResponseDTO(repository.save(role));
	}

	@Transactional
	public RoleResponseDTO deleteRole(Integer id) throws NotFoundException {
		Optional<Role> optional = repository.findById(id);
		if (optional.isPresent()) {
			Role role = optional.get();
			repository.deleteById(id);
			
			return new RoleResponseDTO(role);
		} else {
			throw new NotFoundException("Role", id);
		}
	}
}

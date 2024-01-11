package com.music.merchandisingMS.service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.music.merchandisingMS.dto.StatusRequestDTO;
import com.music.merchandisingMS.dto.StatusResponseDTO;
import com.music.merchandisingMS.exception.NameAlreadyUsedException;
import com.music.merchandisingMS.exception.NotFoundException;
import com.music.merchandisingMS.model.Status;
import com.music.merchandisingMS.repository.StatusRepository;

@Service("statusService")
public class StatusService {

	@Autowired
	private StatusRepository repository;
	
	@Transactional(readOnly = true)
	public List<StatusResponseDTO> findAll() {
		return repository.findAll()
				.stream()
				.map( StatusResponseDTO::new ).toList();
	}
	
	@Transactional(readOnly = true)
	public StatusResponseDTO findById(Integer id) throws NotFoundException {
		Optional<Status> optional = repository.findById(id);
		if (optional.isPresent()) {
			Status status = optional.get();
			return new StatusResponseDTO(status);
		} else {
			throw new NotFoundException("Status", id);
		}
	}
	
	@Transactional
	public StatusResponseDTO saveStatus(StatusRequestDTO request) throws NameAlreadyUsedException {
		Optional<Status> optional = repository.findByName(request.getName());
		if (!optional.isPresent()) {
			Status status = new Status(request);
			return new StatusResponseDTO(repository.save(status));
		} else {
			throw new NameAlreadyUsedException("Status", request.getName());
		}
	}
	
	@Transactional
	public StatusResponseDTO updateStatus(Integer id, StatusRequestDTO request) throws NameAlreadyUsedException, NotFoundException {
		Optional<Status> optional = repository.findByName(request.getName());
		if (optional.isPresent()) {
			throw new NameAlreadyUsedException("Status", request.getName());
		}
		
		optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new NotFoundException("Status", id);
		}
		
		Status status = optional.get();
		status.setName(request.getName());
		
		return new StatusResponseDTO(repository.save(status));
	}
	
	@Transactional
	public StatusResponseDTO deleteStatus(Integer id) throws NotFoundException, SQLIntegrityConstraintViolationException {
		Optional<Status> optional = repository.findById(id);
		if (optional.isPresent()) {
			Status status = optional.get();
			repository.deleteById(id);
			
			return new StatusResponseDTO(status);
		} else {
			throw new NotFoundException("Status", id);
		}
	}
}

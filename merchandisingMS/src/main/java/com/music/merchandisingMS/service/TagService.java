package com.music.merchandisingMS.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.music.merchandisingMS.dto.TagRequestDTO;
import com.music.merchandisingMS.dto.TagResponseDTO;
import com.music.merchandisingMS.exception.NameAlreadyUsedException;
import com.music.merchandisingMS.exception.NotFoundException;
import com.music.merchandisingMS.model.Tag;
import com.music.merchandisingMS.repository.TagRepository;

@Service("tagService")
public class TagService {

	@Autowired
	private TagRepository repository;
	
	@Transactional(readOnly = true)
	public List<TagResponseDTO> findAll() {
		return repository.findAll()
				.stream()
				.map( TagResponseDTO::new ).toList();
	}
	
	@Transactional(readOnly = true)
	public TagResponseDTO findById(Integer id) throws NotFoundException {
		Optional<Tag> optional = repository.findById(id);
		if (optional.isPresent()) {
			Tag tag = optional.get();
			return new TagResponseDTO(tag);
		} else {
			throw new NotFoundException("Tag", id);
		}
	}
	
	@Transactional
	public TagResponseDTO saveTag(TagRequestDTO request) throws NameAlreadyUsedException {
		Optional<Tag> optional = repository.findByName(request.getName());
		if (!optional.isPresent()) {
			Tag tag = new Tag(request);
			return new TagResponseDTO(repository.save(tag));
		} else {
			throw new NameAlreadyUsedException("Tag", request.getName());
		}
	}
	
	@Transactional
	public TagResponseDTO updateTag(Integer id, TagRequestDTO request) throws NotFoundException, NameAlreadyUsedException {
		Optional<Tag> optional = repository.findByName(request.getName());
		if (optional.isPresent()) {
			throw new NameAlreadyUsedException("Tag", request.getName());
		}
		
		optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new NotFoundException("Tag", id);
		}
		
		Tag tag = optional.get();
		tag.setName(request.getName());
		
		return new TagResponseDTO(repository.save(tag));
	}
	
	@Transactional
	public TagResponseDTO deleteTag(Integer id) throws NotFoundException {
		Optional<Tag> optional = repository.findById(id);
		if (optional.isPresent()) {
			Tag tag = optional.get();
			repository.deleteById(id);
			
			return new TagResponseDTO(tag);
		} else {
			throw new NotFoundException("Tag", id);
		}
	}
}

package com.music.merchandisingMS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.music.merchandisingMS.dto.TagRequestDTO;
import com.music.merchandisingMS.dto.TagResponseDTO;
import com.music.merchandisingMS.exception.NameAlreadyUsedException;
import com.music.merchandisingMS.exception.NotFoundException;
import com.music.merchandisingMS.service.TagService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController("tagController")
@RequestMapping("/api/tag")
public class TagController {

	@Autowired
	private TagService service;
	
	@GetMapping("")
	public ResponseEntity<List<TagResponseDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping("/{id}")
	public  ResponseEntity<TagResponseDTO> findById(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@PostMapping("")
	public ResponseEntity<TagResponseDTO> saveTag(@RequestBody @Valid TagRequestDTO request) throws NameAlreadyUsedException {
		return new ResponseEntity<>(service.saveTag(request), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<TagResponseDTO> updateTag(@PathVariable Integer id, @RequestBody @Valid TagRequestDTO request) throws NotFoundException, NameAlreadyUsedException {
		return ResponseEntity.ok(service.updateTag(id, request));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<TagResponseDTO> deleteTag(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.deleteTag(id));
	}
}

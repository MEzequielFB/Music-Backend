package com.music.merchandisingMS.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.music.merchandisingMS.dto.StatusRequestDTO;
import com.music.merchandisingMS.dto.StatusResponseDTO;
import com.music.merchandisingMS.exception.NameAlreadyUsedException;
import com.music.merchandisingMS.exception.NotFoundException;
import com.music.merchandisingMS.service.StatusService;

import jakarta.validation.Valid;

@RestController("statusController")
@RequestMapping("/api/status")
public class StatusController {

	@Autowired
	private StatusService service;
	
	@GetMapping("")
	public ResponseEntity<List<StatusResponseDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<StatusResponseDTO> findById(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@PostMapping("")
	public ResponseEntity<StatusResponseDTO> saveStatus(@RequestBody @Valid StatusRequestDTO request) throws NameAlreadyUsedException {
		return new ResponseEntity<StatusResponseDTO>(service.saveStatus(request), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<StatusResponseDTO> updateStatus(@PathVariable Integer id, @RequestBody @Valid StatusRequestDTO request) throws NameAlreadyUsedException, NotFoundException {
		return ResponseEntity.ok(service.updateStatus(id, request));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<StatusResponseDTO> deleteStatus(@PathVariable Integer id) throws NotFoundException, SQLIntegrityConstraintViolationException {
		return ResponseEntity.ok(service.deleteStatus(id));
	}
}

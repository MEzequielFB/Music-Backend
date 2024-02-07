package com.music.userMS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.music.userMS.dto.RoleRequestDTO;
import com.music.userMS.dto.RoleResponseDTO;
import com.music.userMS.exception.NameAlreadyUsedException;
import com.music.userMS.exception.NotFoundException;
import com.music.userMS.model.Roles;
import com.music.userMS.service.RoleService;

import jakarta.validation.Valid;

@RestController(value = "roleController")
@RequestMapping("/api/role")
public class RoleController {

	@Autowired
	private RoleService service;
	
	@GetMapping("")
	@PreAuthorize( "hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')" )
	public ResponseEntity<List<RoleResponseDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping("/{id}")
	@PreAuthorize( "hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')" )
	public ResponseEntity<RoleResponseDTO> findById(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@PostMapping("")
	@PreAuthorize( "hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')" )
	public ResponseEntity<RoleResponseDTO> saveRole(@RequestBody @Valid RoleRequestDTO request) throws NameAlreadyUsedException {
		return ResponseEntity.ok(service.saveRole(request));
	}
}

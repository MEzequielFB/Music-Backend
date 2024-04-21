package com.music.userMS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController(value = "roleController")
@RequestMapping("/api/role")
@Tag(name = "Role Controller", description = "<p>Used for role management: create and return roles</p>")
public class RoleController {

	@Autowired
	private RoleService service;
	
	@Operation(summary = "Find all roles", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("")
	@PreAuthorize( "hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')" )
	public ResponseEntity<List<RoleResponseDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@Operation(summary = "Find role by id", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Role id", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode =  "200", description = "Role found", content = {
			@Content(schema = @Schema(implementation = RoleResponseDTO.class))
		}),
		@ApiResponse(responseCode =  "404", description = "Role not found", content = {
			@Content(schema = @Schema(example = "The entity Role with id 1 doesn't exist"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/{id}")
	@PreAuthorize( "hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')" )
	public ResponseEntity<RoleResponseDTO> findById(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@Operation(summary = "Save role", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ")
	@ApiResponses(value = {
		@ApiResponse(responseCode =  "201", description = "Role saved", content = {
			@Content(schema = @Schema(implementation = RoleResponseDTO.class))
		}),
		@ApiResponse(responseCode =  "400", description = "Role's name already in used", content = {
			@Content(schema = @Schema(example = "A Role with the name 'USER' already exists"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@PostMapping("")
	@PreAuthorize( "hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')" )
	public ResponseEntity<RoleResponseDTO> saveRole(@RequestBody @Valid RoleRequestDTO request) throws NameAlreadyUsedException {
		return new ResponseEntity<RoleResponseDTO>(service.saveRole(request), HttpStatus.CREATED);
	}
}

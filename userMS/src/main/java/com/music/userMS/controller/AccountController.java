package com.music.userMS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.music.userMS.dto.AccountRequestDTO;
import com.music.userMS.dto.AccountResponseDTO;
import com.music.userMS.dto.BalanceDTO;
import com.music.userMS.dto.UserIdDTO;
import com.music.userMS.exception.AddUserException;
import com.music.userMS.exception.AlreadyContainsException;
import com.music.userMS.exception.AuthorizationException;
import com.music.userMS.exception.MultipleUsersLinkedToAccountException;
import com.music.userMS.exception.NotEnoughBalanceException;
import com.music.userMS.exception.NotFoundException;
import com.music.userMS.exception.SomeEntityDoesNotExistException;
import com.music.userMS.model.Roles;
import com.music.userMS.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController(value = "accountController")
@RequestMapping("api/account")
public class AccountController {

	@Autowired
	private AccountService service;
	
	@Operation(summary = "Find all accounts", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("")
	@PreAuthorize( "hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')" )
	public ResponseEntity<List<AccountResponseDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@Operation(summary = "Find account by id", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/{id}")
	@PreAuthorize( "hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')" )
	public ResponseEntity<AccountResponseDTO> findById(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@Operation(summary = "Find all accounts by userId", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/user/{userId}")
	@PreAuthorize( "hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')" )
	public ResponseEntity<List<AccountResponseDTO>> findByAllByUser(@PathVariable Integer userId) throws NotFoundException {
		return ResponseEntity.ok(service.findByAllByUser(userId));
	}
	
	@Operation(summary = "Save account", description = "<p>Required roles:</p> <ul><li>USER</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@PostMapping("")
	@PreAuthorize("hasAuthority('" + Roles.USER + "')")
	public ResponseEntity<AccountResponseDTO> saveAccount(@RequestBody @Valid AccountRequestDTO request, @RequestHeader("Authorization") String token) throws SomeEntityDoesNotExistException, AuthorizationException, NotFoundException, AddUserException {
		return new ResponseEntity<AccountResponseDTO>(service.saveAccount(request, token), HttpStatus.CREATED);
	}
	
	@Operation(summary = "Add a user to an account. The logged user should be linked to the account to add another user", description = "<p>Required roles:</p> <ul><li>USER</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("/{id}/addUser")
	@PreAuthorize("hasAuthority('" + Roles.USER + "')")
	public ResponseEntity<AccountResponseDTO> addUser(@PathVariable Integer id, @RequestBody @Valid UserIdDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, AlreadyContainsException, AuthorizationException, AddUserException {
		return ResponseEntity.ok(service.addUser(id, request.getUserId(), token));
	}
	
	@Operation(summary = "Remove a user from an account. The logged user should be linked to the account to remove another user", description = "<p>Required roles:</p> <ul><li>USER</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("/{id}/removeUser")
	@PreAuthorize("hasAuthority('" + Roles.USER + "')")
	public ResponseEntity<AccountResponseDTO> removeUser(@PathVariable Integer id, @RequestBody @Valid UserIdDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, AuthorizationException {
		return ResponseEntity.ok(service.removeUser(id, request.getUserId(), token));
	}
	
	@Operation(summary = "Add balance to an account. The logged user should be linked to the account or be an administrator", description = "<p>Required roles:</p> <ul><li>USER</li><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("/{id}/addBalance")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "')")
	public ResponseEntity<AccountResponseDTO> addBalance(@PathVariable Integer id, @RequestBody @Valid BalanceDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, AuthorizationException {
		return ResponseEntity.ok(service.addBalance(id, request, token));
	}
	
	@Operation(summary = "Remove balance from an account. The logged user should be linked to the account or be an administrator", description = "<p>Required roles:</p> <ul><li>USER</li><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("/{id}/removeBalance")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "')")
	public ResponseEntity<AccountResponseDTO> removeBalance(@PathVariable Integer id, @RequestBody @Valid BalanceDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, NotEnoughBalanceException, AuthorizationException {
		return ResponseEntity.ok(service.removeBalance(id, request, token));
	}
	
	@Operation(summary = "Delete an account. The logged user should be linked to the account or be an administrator. There should be one or zero users linked to the account for delete it", description = "<p>Required roles:</p> <ul><li>USER</li><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "')")
	public ResponseEntity<AccountResponseDTO> deleteAccount(@PathVariable Integer id, @RequestHeader("Authorization") String token) throws NotFoundException, MultipleUsersLinkedToAccountException, AuthorizationException {
		return ResponseEntity.ok(service.deleteAccount(id, token));
	}
}

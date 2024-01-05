package com.music.userMS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.music.userMS.dto.AccountRequestDTO;
import com.music.userMS.dto.AccountResponseDTO;
import com.music.userMS.dto.UserIdDTO;
import com.music.userMS.exception.AlreadyContainsException;
import com.music.userMS.exception.MultipleUsersLinkedToAccountException;
import com.music.userMS.exception.NotFoundException;
import com.music.userMS.exception.SomeEntityDoesNotExistException;
import com.music.userMS.service.AccountService;

import jakarta.validation.Valid;

@RestController(value = "accountController")
@RequestMapping("api/account")
public class AccountController {

	@Autowired
	private AccountService service;
	
	@GetMapping("")
	public ResponseEntity<List<AccountResponseDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<AccountResponseDTO> findById(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@PostMapping("")
	public ResponseEntity<AccountResponseDTO> saveAccount(@RequestBody @Valid AccountRequestDTO request) throws SomeEntityDoesNotExistException {
		return ResponseEntity.ok(service.saveAccount(request));
	}
	
	@PutMapping("/{id}/addUser")
	public ResponseEntity<AccountResponseDTO> addUser(@PathVariable Integer id, @RequestBody @Valid UserIdDTO request) throws NotFoundException, AlreadyContainsException {
		return ResponseEntity.ok(service.addUser(id, request.getUserId()));
	}
	
	@PutMapping("/{id}/removeUser")
	public ResponseEntity<AccountResponseDTO> removeUser(@PathVariable Integer id, @RequestBody @Valid UserIdDTO request) throws NotFoundException {
		return ResponseEntity.ok(service.removeUser(id, request.getUserId()));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<AccountResponseDTO> deleteAccount(@PathVariable Integer id) throws NotFoundException, MultipleUsersLinkedToAccountException {
		return ResponseEntity.ok(service.deleteAccount(id));
	}
}

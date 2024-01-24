package com.music.userMS.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.music.userMS.dto.AccountRequestDTO;
import com.music.userMS.dto.AccountResponseDTO;
import com.music.userMS.dto.BalanceDTO;
import com.music.userMS.exception.AlreadyContainsException;
import com.music.userMS.exception.MultipleUsersLinkedToAccountException;
import com.music.userMS.exception.NotEnoughBalanceException;
import com.music.userMS.exception.NotFoundException;
import com.music.userMS.exception.SomeEntityDoesNotExistException;
import com.music.userMS.model.Account;
import com.music.userMS.model.User;
import com.music.userMS.repository.AccountRepository;
import com.music.userMS.repository.UserRepository;

@Service(value = "accountService")
public class AccountService {

	@Autowired
	private AccountRepository repository;
	
	@Autowired
	private UserRepository userRepository;
	
	private static Integer LIMIT_USERS_FOR_DELETE = 1;
	
	@Transactional(readOnly = true)
	public List<AccountResponseDTO> findAll() {
		return repository.findAll()
				.stream()
				.map( AccountResponseDTO::new ).toList();
	}
	
	@Transactional(readOnly = true)
	public AccountResponseDTO findById(Integer id) throws NotFoundException {
		Optional<Account> optional = repository.findById(id);
		if (optional.isPresent()) {
			Account account = optional.get();
			return new AccountResponseDTO(account);
		} else {
			throw new NotFoundException("Account", id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<AccountResponseDTO> findByAllByUser(Integer userId) throws NotFoundException {
		Optional<User> optional = userRepository.findById(userId);
		if (optional.isPresent()) {
			User user = optional.get();
			
			return repository.findAllByUser(user)
					.stream()
					.map( AccountResponseDTO::new )
					.toList();
		} else {
			throw new NotFoundException("User", userId);
		}
	}
	
	@Transactional
	public AccountResponseDTO saveAccount(AccountRequestDTO request) throws SomeEntityDoesNotExistException {
		List<User> users = userRepository.findAllByIds(request.getUsersId());
		
		if (users.size() != request.getUsersId().size()) {
			throw new SomeEntityDoesNotExistException("User");
		}
		
		Account account = new Account(request, users);
		return new AccountResponseDTO(repository.save(account));
	}
	
	@Transactional
	public AccountResponseDTO addUser(Integer id, Integer userId) throws NotFoundException, AlreadyContainsException {
		Optional<Account> optional = repository.findById(id);
		Optional<User> userOptional = userRepository.findById(userId);
		
		if (!optional.isPresent()) {
			throw new NotFoundException("Account", id);
		}
		if (!userOptional.isPresent() || userOptional.get().getIsDeleted()) {
			throw new NotFoundException("User", userId);
		}
		
		Account account = optional.get();
		User user = userOptional.get();
		
		if (repository.accountContainsUser(account, user)) {
			throw new AlreadyContainsException(account, user);
		}
		
		account.addUser(user);
		
		return new AccountResponseDTO(repository.save(account));
	}
	
	@Transactional
	public AccountResponseDTO removeUser(Integer id, Integer userId) throws NotFoundException {
		Optional<Account> optional = repository.findById(id);
		Optional<User> userOptional = userRepository.findById(userId);
		
		if (!optional.isPresent()) {
			throw new NotFoundException("Account", id);
		}
		if (!userOptional.isPresent()) {
			throw new NotFoundException("User", userId);
		}
		
		Account account = optional.get();
		User user = userOptional.get();
		
		account.removeUser(user);
		
		return new AccountResponseDTO(repository.save(account));
	}
	
	@Transactional
	public AccountResponseDTO addBalance(Integer id, BalanceDTO request) throws NotFoundException {
		Optional<Account> optional = repository.findById(id);
		if (optional.isPresent()) {
			Account account = optional.get();
			Double newBalance = Math.round(( account.getBalance() + request.getBalance() ) * 100.0) / 100.0;
			
			account.setBalance(newBalance);
			
			return new AccountResponseDTO(repository.save(account));
		} else {
			throw new NotFoundException("Account", id);
		}
	}
	
	@Transactional
	public AccountResponseDTO removeBalance(Integer id, BalanceDTO request) throws NotFoundException, NotEnoughBalanceException {
		Optional<Account> optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new NotFoundException("Account", id);
		}
			
		Account account = optional.get();
		
		if (account.getBalance() < request.getBalance()) {
			throw new NotEnoughBalanceException(id);
		}
		
		Double newBalance = Math.round(( account.getBalance() - request.getBalance() ) * 100.0) / 100.0;
		account.setBalance(newBalance);
		
		return new AccountResponseDTO(repository.save(account));
	}
	
	// Delete just when it has zero or one users linked
	@Transactional
	public AccountResponseDTO deleteAccount(Integer id) throws NotFoundException, MultipleUsersLinkedToAccountException {
		Optional<Account> optional = repository.findById(id);
		if (optional.isPresent()) {
			Account account = optional.get();
			
			if (account.getUsers().size() > LIMIT_USERS_FOR_DELETE) {
				throw new MultipleUsersLinkedToAccountException(id);
			}
			
			account.setUsers(new ArrayList<>());
			repository.save(account);
			repository.deleteById(id);
			
			return new AccountResponseDTO(account);
		} else {
			throw new NotFoundException("Account", id);
		}
	}
}

package com.music.userMS.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.music.userMS.dto.AccountRequestDTO;
import com.music.userMS.dto.AccountResponseDTO;
import com.music.userMS.dto.BalanceDTO;
import com.music.userMS.exception.AddUserException;
import com.music.userMS.exception.AlreadyContainsException;
import com.music.userMS.exception.AuthorizationException;
import com.music.userMS.exception.MultipleUsersLinkedToAccountException;
import com.music.userMS.exception.NotEnoughBalanceException;
import com.music.userMS.exception.NotFoundException;
import com.music.userMS.exception.SomeEntityDoesNotExistException;
import com.music.userMS.model.Account;
import com.music.userMS.model.Roles;
import com.music.userMS.model.User;
import com.music.userMS.repository.AccountRepository;
import com.music.userMS.repository.UserRepository;

@Service(value = "accountService")
public class AccountService {

	@Autowired
	private AccountRepository repository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private WebClient webClient;
	
	@Value("${app.api.domain}")
	private String domain;
	
	@Value("${app.api.authms.domain}")
	private String authmsDomain;
	
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
	
	@Transactional(readOnly = true)
	public List<AccountResponseDTO> findByAllByLoggedUser(String token) throws AuthorizationException, NotFoundException {
		Integer loggedUserId = null;
		try {
			loggedUserId = webClient
					.get()
					.uri(String.format("%s/api/auth/id", this.authmsDomain))
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(Integer.class)
					.block();
		} catch (Exception e) {
			System.err.println(e);
			throw new AuthorizationException();
		}
		
		Optional<User> userOptional = userRepository.findById(loggedUserId);
		
		if (!userOptional.isPresent()) {
			throw new NotFoundException("User", loggedUserId);
		}
		
		User user = userOptional.get();
		
		return repository.findAllByUser(user)
				.stream()
				.map(AccountResponseDTO::new)
				.toList();
	}
	
	@Transactional
	public AccountResponseDTO saveAccount(AccountRequestDTO request, String token) throws SomeEntityDoesNotExistException, AuthorizationException, NotFoundException, AddUserException {
		Integer loggedUserId = null;
		try {
			loggedUserId = webClient
					.get()
					.uri(String.format("%s/api/auth/id", this.authmsDomain))
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(Integer.class)
					.block();
		} catch (Exception e) {
			System.err.println(e);
			throw new AuthorizationException();
		}
		
		Optional<User> userOptional = userRepository.findById(loggedUserId);
		
		if (!userOptional.isPresent()) {
			throw new NotFoundException("User", loggedUserId);
		}
		
		User user = userOptional.get();
		
		if (!user.getRole().getName().equals(Roles.USER)) {
			throw new AddUserException(loggedUserId);
		}
		
		Set<User> users = new HashSet<>();
		users.add(user);
		
		Account account = new Account(request, users);
		return new AccountResponseDTO(repository.save(account));
	}
	
	@Transactional
	public AccountResponseDTO addUser(Integer id, Integer userId, String token) throws NotFoundException, AlreadyContainsException, AuthorizationException, AddUserException {
		Integer loggedUserId = null;
		try {
			loggedUserId = webClient
					.get()
					.uri(String.format("%s/api/auth/id", this.authmsDomain))
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(Integer.class)
					.block();
		} catch (Exception e) {
			System.err.println(e);
			throw new AuthorizationException();
		}
		
		Optional<Account> optional = repository.findById(id);
		Optional<User> userOptional = userRepository.findById(userId);
		Optional<User> loggedUserOptional = userRepository.findById(loggedUserId);
		
		if (!optional.isPresent()) {
			throw new NotFoundException("Account", id);
		}
		if (!userOptional.isPresent() || userOptional.get().getIsDeleted()) {
			throw new NotFoundException("User", userId);
		}
		
		Account account = optional.get();
		User user = userOptional.get();
		User loggedUser = loggedUserOptional.get();
		
		// if the logged user is not in the account and tries to add another user throw exception
		if (!repository.accountContainsUser(account, loggedUser)) {
			throw new AuthorizationException();
		}
		if (repository.accountContainsUser(account, user)) {
			throw new AlreadyContainsException(account, user);
		}
		if (!user.getRole().getName().equals(Roles.USER)) {
			throw new AddUserException(userId);
		}
		
		account.addUser(user);
		
		return new AccountResponseDTO(repository.save(account));
	}
	
	@Transactional
	public AccountResponseDTO removeUser(Integer id, Integer userId, String token) throws NotFoundException, AuthorizationException {
		Integer loggedUserId = null;
		try {
			loggedUserId = webClient
					.get()
					.uri(String.format("%s/api/auth/id", this.authmsDomain))
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(Integer.class)
					.block();
		} catch (Exception e) {
			System.err.println(e);
			throw new AuthorizationException();
		}
		
		Optional<Account> optional = repository.findById(id);
		Optional<User> userOptional = userRepository.findById(userId);
		Optional<User> loggedUserOptional = userRepository.findById(loggedUserId);
		
		if (!optional.isPresent()) {
			throw new NotFoundException("Account", id);
		}
		if (!userOptional.isPresent()) {
			throw new NotFoundException("User", userId);
		}
		
		Account account = optional.get();
		User user = userOptional.get();
		User loggedUser = loggedUserOptional.get();
		
		// if the logged user is not in the account and tries to add another user throw exception
		if (!repository.accountContainsUser(account, loggedUser)) {
			throw new AuthorizationException();
		}
		
		account.removeUser(user);
		
		return new AccountResponseDTO(repository.save(account));
	}
	
	@Transactional
	public AccountResponseDTO addBalance(Integer id, BalanceDTO request, String token) throws NotFoundException, AuthorizationException {
		Integer loggedUserId = null;
		try {
			loggedUserId = webClient
					.get()
					.uri(String.format("%s/api/auth/id", this.authmsDomain))
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(Integer.class)
					.block();
		} catch (Exception e) {
			System.err.println(e);
			throw new AuthorizationException();
		}
		
		Optional<Account> optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new NotFoundException("Account", id);
		}
		
		Optional<User> userOptional = userRepository.findById(loggedUserId);
		if (!userOptional.isPresent()) {
			throw new NotFoundException("User", loggedUserId);
		}
		
		Account account = optional.get();
		User user = userOptional.get();
		
		// if the account doesn't contain the logged user and the logged user is not an admin or super admin throws exception
		Boolean containsUser = repository.accountContainsUser(account, user);
		if (!containsUser && !(user.getRole().getName().equals(Roles.ADMIN) || user.getRole().getName().equals(Roles.SUPER_ADMIN))) {
			throw new AuthorizationException();
		}
		
		Double newBalance = Math.round(( account.getBalance() + request.getBalance() ) * 100.0) / 100.0;
		
		account.setBalance(newBalance);
		
		return new AccountResponseDTO(repository.save(account));
	}
	
	@Transactional
	public AccountResponseDTO removeBalance(Integer id, BalanceDTO request, String token) throws NotFoundException, NotEnoughBalanceException, AuthorizationException {
		Integer loggedUserId = null;
		try {
			loggedUserId = webClient
					.get()
					.uri(String.format("%s/api/auth/id", this.authmsDomain))
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(Integer.class)
					.block();
		} catch (Exception e) {
			System.err.println(e);
			throw new AuthorizationException();
		}
		
		Optional<Account> optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new NotFoundException("Account", id);
		}
		
		Optional<User> userOptional = userRepository.findById(loggedUserId);
		if (!userOptional.isPresent()) {
			throw new NotFoundException("User", loggedUserId);
		}
			
		Account account = optional.get();
		User user = userOptional.get();
		
		// if the account doesn't contain the logged user and the logged user is not an admin or super admin throws exception
		Boolean containsUser = repository.accountContainsUser(account, user);
		if (!containsUser && !(user.getRole().getName().equals(Roles.ADMIN) || user.getRole().getName().equals(Roles.SUPER_ADMIN))) {
			throw new AuthorizationException();
		}
		
		if (account.getBalance() < request.getBalance()) {
			throw new NotEnoughBalanceException(id);
		}
		
		Double newBalance = Math.round(( account.getBalance() - request.getBalance() ) * 100.0) / 100.0;
		account.setBalance(newBalance);
		
		return new AccountResponseDTO(repository.save(account));
	}
	
	// Delete just when it has zero or one users linked
	@Transactional
	public AccountResponseDTO deleteAccount(Integer id, String token) throws NotFoundException, MultipleUsersLinkedToAccountException, AuthorizationException {
		Integer loggedUserId = null;
		try {
			loggedUserId = webClient
					.get()
					.uri(String.format("%s/api/auth/id", this.authmsDomain))
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(Integer.class)
					.block();
		} catch (Exception e) {
			System.err.println(e);
			throw new AuthorizationException();
		}
		
		Optional<User> userOptional = userRepository.findById(loggedUserId);
		
		if (!userOptional.isPresent()) {
			throw new NotFoundException("User", loggedUserId);
		}
		
		User user = userOptional.get();
		
		Optional<Account> optional = repository.findById(id);
		
		if (!optional.isPresent()) {
			throw new NotFoundException("Account", id);
		}
			
		Account account = optional.get();
		
		if (!account.containsUser(user) && (!user.getRole().getName().equals(Roles.ADMIN) && !user.getRole().getName().equals(Roles.SUPER_ADMIN))) {
			throw new AuthorizationException();
		}
		if (account.getUsers().size() > LIMIT_USERS_FOR_DELETE) {
			throw new MultipleUsersLinkedToAccountException(id);
		}
		
		account.setUsers(new HashSet<>());
		repository.save(account);
		repository.deleteById(id);
		
		return new AccountResponseDTO(account);
	}
}

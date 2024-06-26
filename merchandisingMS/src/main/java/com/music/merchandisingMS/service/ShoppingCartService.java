package com.music.merchandisingMS.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.music.merchandisingMS.dto.AccountDTO;
import com.music.merchandisingMS.dto.BalanceDTO;
import com.music.merchandisingMS.dto.ProductQuantityRequestDTO;
import com.music.merchandisingMS.dto.ShoppingCartRequestDTO;
import com.music.merchandisingMS.dto.ShoppingCartResponseDTO;
import com.music.merchandisingMS.dto.UserDTO;
import com.music.merchandisingMS.exception.AuthorizationException;
import com.music.merchandisingMS.exception.EmptyShoppingCartException;
import com.music.merchandisingMS.exception.EntityWithUserIdAlreadyUsedException;
import com.music.merchandisingMS.exception.NotFoundException;
import com.music.merchandisingMS.exception.PermissionsException;
import com.music.merchandisingMS.exception.StockException;
import com.music.merchandisingMS.model.Order;
import com.music.merchandisingMS.model.OrderStatus;
import com.music.merchandisingMS.model.Product;
import com.music.merchandisingMS.model.ShoppingCart;
import com.music.merchandisingMS.model.Status;
import com.music.merchandisingMS.repository.OrderRepository;
import com.music.merchandisingMS.repository.ProductRepository;
import com.music.merchandisingMS.repository.ShoppingCartRepository;
import com.music.merchandisingMS.repository.StatusRepository;

@Service("shoppingCartService")
public class ShoppingCartService {

	@Autowired
	private ShoppingCartRepository repository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private StatusRepository statusRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private WebClient webClient;
	
	@Value("${app.api.domain}")
	private String domain;
	
	@Value("${app.api.userms.domain}")
	private String usermsDomain;
	
	@Value("${app.api.authms.domain}")
	private String authmsDomain;
	
	@Transactional(readOnly = true)
	public List<ShoppingCartResponseDTO> findAll(String token) {
		return repository.findAll()
				.stream()
				.map(shoppingCart -> {
					UserDTO user = null;
					user = webClient
							.get()
							.uri(String.format("%s/api/user/%s", this.usermsDomain, shoppingCart.getUserId()))
							.header("Authorization", token)
							.retrieve()
							.bodyToMono(UserDTO.class)
							.block();
					
					return new ShoppingCartResponseDTO(shoppingCart, user);
				})
				.toList();
	}
	
	@Transactional(readOnly = true)
	public ShoppingCartResponseDTO findById(Integer id, String token) throws NotFoundException {
		Optional<ShoppingCart> optional = repository.findById(id);
		if (optional.isPresent()) {
			ShoppingCart shoppingCart = optional.get();
			
			UserDTO user = null;
			try {
				user = webClient
						.get()
						.uri(String.format("%s/api/user/%s", this.usermsDomain, shoppingCart.getUserId()))
						.header("Authorization", token)
						.retrieve()
						.bodyToMono(UserDTO.class)
						.block();
			} catch (Exception e) {
				throw new NotFoundException("User", shoppingCart.getUserId());
			}
			
			return new ShoppingCartResponseDTO(shoppingCart, user);
		} else {
			throw new NotFoundException("ShoppingCart", id);
		}
	}
	
	@Transactional
	public ShoppingCartResponseDTO findByLoggedUser(String token) throws AuthorizationException, NotFoundException {
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
		
		Optional<ShoppingCart> optional = repository.findByUserId(loggedUserId);
		if (!optional.isPresent()) {
			throw new NotFoundException("ShoppingCart", loggedUserId);
		}
		
		ShoppingCart shoppingCart = optional.get();
		
		UserDTO user = null; 
		try {
			user = webClient
					.get()
					.uri(String.format("%s/api/user/%s", this.usermsDomain, loggedUserId))
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(UserDTO.class)
					.block();
		} catch (Exception e) {
			throw new NotFoundException("User", loggedUserId);
		}
		
		return new ShoppingCartResponseDTO(shoppingCart, user);
	}
	
	@Transactional
	public ShoppingCartResponseDTO saveShoppingCart(ShoppingCartRequestDTO request, String token) throws EntityWithUserIdAlreadyUsedException, NotFoundException, StockException, AuthorizationException {
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
		
		Optional<ShoppingCart> optional = repository.findByUserId(loggedUserId);
		if (optional.isPresent()) {
			throw new EntityWithUserIdAlreadyUsedException("ShoppingCart", loggedUserId);
		}
		
		UserDTO user = null;
		try {
			user = webClient
					.get()
					.uri(String.format("%s/api/user/%s", this.usermsDomain, loggedUserId))
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(UserDTO.class)
					.block();
		} catch (Exception e) {
			throw new NotFoundException("User", loggedUserId);
		}
		
		Set<Product> products = new HashSet<>();
		for (Integer productId : request.getProducts()) {
			Optional<Product> productOptional = productRepository.findById(productId);
			if (!productOptional.isPresent() || productOptional.get().getIsDeleted()) {
				throw new NotFoundException("Product", productId);
			}
			
			Product product = productOptional.get();
			products.add(product);
		}
		
		ShoppingCart shoppingCart = new ShoppingCart(loggedUserId, products);
		Double totalPrice = 0.0;
		
		for (Product product : products) {
			Integer productQuantity = shoppingCart.getQuantityOfProduct(product);
			if (productQuantity > product.getStock()) {
				throw new StockException(product);
			}
			
			totalPrice += product.getPrice() * (1.0 - (product.getDiscount() / 100));
		}
		
		totalPrice = Math.round(totalPrice * 100.0) / 100.0;
		shoppingCart.setTotalPrice(totalPrice);
		
		return new ShoppingCartResponseDTO(repository.save(shoppingCart), user);
	}
	
	@Transactional
	public ShoppingCartResponseDTO buyProducts(Integer id, Integer accountId, String token) throws NotFoundException, EmptyShoppingCartException, StockException, AuthorizationException {
		Optional<ShoppingCart> optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new NotFoundException("ShoppingCart", id);
		}
		
		ShoppingCart shoppingCart = optional.get();
		if (shoppingCart.getProducts().isEmpty()) {
			throw new EmptyShoppingCartException(id);
		}
		
		HashMap<Product, Integer> visitedProducts = new HashMap<>();
		for (Product product : shoppingCart.getProducts()) {
			if (!visitedProducts.containsKey(product)) {
				Integer productQuantity = shoppingCart.getQuantityOfProduct(product);
				if (productQuantity > product.getStock()) {
					throw new StockException(product);
				}
				
				visitedProducts.put(product, productQuantity);
			}
		}
		
		webClient // EXCEPTION HANDLE FOR NOT ENOUGH BALANCE, ACCOUNT NOT FOUND AND AUTHORIZATION ISSUE
				.put()
				.uri(String.format("%s/api/account/%s/removeBalance", this.usermsDomain, accountId))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.header("Authorization", token)
				.body(BodyInserters.fromValue(new BalanceDTO(shoppingCart.getTotalPrice())))
				.retrieve()
				.bodyToMono(AccountDTO.class)
				.block();
		
		UserDTO user = null;
		try {
			user = webClient
					.get()
					.uri(String.format("%s/api/user/%s", this.usermsDomain, shoppingCart.getUserId()))
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(UserDTO.class)
					.block();
		} catch (Exception e) {
			throw new NotFoundException("User", shoppingCart.getUserId());
		}
		
		for (Entry<Product, Integer> entry : visitedProducts.entrySet()) {
			Product product = entry.getKey();
			Integer quantity =  entry.getValue();
			
			product.setStock(product.getStock() - quantity);
		}
		
		Optional<Status> statusOptional = statusRepository.findByName(OrderStatus.PENDING);
		if (!statusOptional.isPresent()) {
			throw new NotFoundException("Status", OrderStatus.PENDING);
		}
		
		Order order = new Order(user.getId(), user.getAddress(), shoppingCart.getTotalPrice(), statusOptional.get(), shoppingCart.getProducts());
		orderRepository.save(order);
		
		repository.deleteById(id);
		return new ShoppingCartResponseDTO(shoppingCart, user);
	}
	
	@Transactional
	public ShoppingCartResponseDTO addProduct(Integer id, ProductQuantityRequestDTO request, String token) throws NotFoundException, StockException, AuthorizationException, PermissionsException {
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
		
		Optional<ShoppingCart> optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new NotFoundException("ShoppingCart", id);
		}
		
		Optional<Product> productOptional = productRepository.findById(request.getProductId());
		if (!productOptional.isPresent() || productOptional.get().getIsDeleted()) {
			throw new NotFoundException("Product", request.getProductId());
		}
		
		ShoppingCart shoppingCart = optional.get();
		Product product = productOptional.get();
		
		if (!shoppingCart.getUserId().equals(loggedUserId)) {
			throw new PermissionsException(loggedUserId);
		}
		
		if (shoppingCart.getQuantityOfProduct(product) + request.getQuantity() > product.getStock()) {
			throw new StockException(product);
		}
		
		UserDTO user = null;
		try {
			user = webClient
					.get()
//					.uri("http://localhost:8001/api/user/" + shoppingCart.getUserId())
					.uri(String.format("%s/api/user/%s", this.usermsDomain, shoppingCart.getUserId()))
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(UserDTO.class)
					.block();
		} catch (Exception e) {
			throw new NotFoundException("User", shoppingCart.getUserId());
		}
		
//		Double plusPrice = (product.getPrice() * request.getQuantity()) * (1.0 - (product.getDiscount() / 100));
//		Double totalPrice =  Math.round((shoppingCart.getTotalPrice() + plusPrice) * 100.0) / 100.0;
		
		shoppingCart.addProduct(product, request.getQuantity());
//		shoppingCart.setTotalPrice(totalPrice);
		
		return new ShoppingCartResponseDTO(repository.save(shoppingCart), user);
	}
	
	@Transactional
	public ShoppingCartResponseDTO removeProduct(Integer id, Integer productId, String token) throws NotFoundException, AuthorizationException, PermissionsException {
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
		
		Optional<ShoppingCart> optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new NotFoundException("ShoppingCart", id);
		}
		
		Optional<Product> productOptional = productRepository.findById(productId);
		if (!productOptional.isPresent()) {
			throw new NotFoundException("Product", productId);
		}
		
		ShoppingCart shoppingCart = optional.get();
		Product product = productOptional.get();
		
		if (!shoppingCart.getUserId().equals(loggedUserId)) {
			throw new PermissionsException(loggedUserId);
		}
		
		UserDTO user = null;
		try {
			user = webClient
					.get()
					.uri(String.format("%s/api/user/%s", this.usermsDomain, shoppingCart.getUserId()))
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(UserDTO.class)
					.block();
		} catch (Exception e) {
			throw new NotFoundException("User", shoppingCart.getUserId());
		}
		
		shoppingCart.removeProduct(product);
//		Integer removedQuantity = shoppingCart.removeProduct(product);
//		
//		Double minusPrice = (product.getPrice() * removedQuantity) * (1.0 - (product.getDiscount() / 100));
//		Double totalPrice =  Math.round((shoppingCart.getTotalPrice() - minusPrice) * 100.0) / 100.0;
//		
//		shoppingCart.setTotalPrice(totalPrice);
		
		return new ShoppingCartResponseDTO(repository.save(shoppingCart), user);
	}
	
	@Transactional
	public ShoppingCartResponseDTO deleteShoppingCart(Integer id, String token) throws NotFoundException {		
		Optional<ShoppingCart> optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new NotFoundException("ShoppingCart", id);
		}
		
		ShoppingCart shoppingCart = optional.get();
		
		UserDTO user = null;
		try {
			user = webClient
					.get()
					.uri(String.format("%s/api/user/%s", this.usermsDomain, shoppingCart.getUserId()))
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(UserDTO.class)
					.block();
		} catch (Exception e) {
			throw new NotFoundException("User", shoppingCart.getUserId());
		}
		
		repository.deleteById(id);
		
		return new ShoppingCartResponseDTO(shoppingCart, user);
	}
}

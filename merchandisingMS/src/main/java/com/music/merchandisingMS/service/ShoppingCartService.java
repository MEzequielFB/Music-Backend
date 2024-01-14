package com.music.merchandisingMS.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.music.merchandisingMS.dto.ProductQuantityRequestDTO;
import com.music.merchandisingMS.dto.ShoppingCartRequestDTO;
import com.music.merchandisingMS.dto.ShoppingCartResponseDTO;
import com.music.merchandisingMS.dto.UserDTO;
import com.music.merchandisingMS.exception.DeletedEntityException;
import com.music.merchandisingMS.exception.EmptyShoppingCartException;
import com.music.merchandisingMS.exception.EntityWithUserIdAlreadyUsedException;
import com.music.merchandisingMS.exception.NotFoundException;
import com.music.merchandisingMS.exception.SomeEntityDoesNotExistException;
import com.music.merchandisingMS.exception.StockException;
import com.music.merchandisingMS.model.Product;
import com.music.merchandisingMS.model.ShoppingCart;
import com.music.merchandisingMS.repository.ProductRepository;
import com.music.merchandisingMS.repository.ShoppingCartRepository;

@Service("shoppingCartService")
public class ShoppingCartService {

	@Autowired
	private ShoppingCartRepository repository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Transactional(readOnly = true)
	public List<ShoppingCartResponseDTO> findAll() {
		return repository.findAll()
				.stream()
				.map(shoppingCart -> {
					UserDTO user = null;
					user = webClientBuilder.build()
							.get()
							.uri("http://localhost:8001/api/user/" + shoppingCart.getUserId())
							.retrieve()
							.bodyToMono(UserDTO.class)
							.block();
					
					return new ShoppingCartResponseDTO(shoppingCart, user);
				})
				.toList();
	}
	
	@Transactional(readOnly = true)
	public ShoppingCartResponseDTO findById(Integer id) throws NotFoundException {
		Optional<ShoppingCart> optional = repository.findById(id);
		if (optional.isPresent()) {
			ShoppingCart shoppingCart = optional.get();
			
			UserDTO user = null;
			try {
				user = webClientBuilder.build()
						.get()
						.uri("http://localhost:8001/api/user/" + shoppingCart.getUserId())
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
	public ShoppingCartResponseDTO saveShoppingCart(ShoppingCartRequestDTO request) throws EntityWithUserIdAlreadyUsedException, SomeEntityDoesNotExistException, NotFoundException, StockException, DeletedEntityException {
		Optional<ShoppingCart> optional = repository.findByUserId(request.getUserId());
		if (optional.isPresent()) {
			throw new EntityWithUserIdAlreadyUsedException("ShoppingCart", request.getUserId());
		}
		
		UserDTO user = null;
		try {
			user = webClientBuilder.build()
					.get()
					.uri("http://localhost:8001/api/user/" + request.getUserId())
					.retrieve()
					.bodyToMono(UserDTO.class)
					.block();
		} catch (Exception e) {
			throw new NotFoundException("User", request.getUserId());
		}
		
		List<Product> products = new ArrayList<>();
		for (Integer productId : request.getProducts()) {
			Optional<Product> productOptional = productRepository.findById(productId);
			if (!productOptional.isPresent()) {
				throw new NotFoundException("Product", productId);
			}
			Product product = productOptional.get();
			if (product.getIsDeleted()) {
				throw new DeletedEntityException("Product", product.getName());
			}
			products.add(product);
		}
		
		ShoppingCart shoppingCart = new ShoppingCart(request, products);
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
	
//	@Transactional
//	public ShoppingCartResponseDTO buyProducts(Integer id, Integer accountId) throws NotFoundException, EmptyShoppingCartException, StockException {
//		Optional<ShoppingCart> optional = repository.findById(id);
//		if (!optional.isPresent()) {
//			throw new NotFoundException("ShoppingCart", id);
//		}
//		
//		ShoppingCart shoppingCart = optional.get();
//		if (shoppingCart.getProducts().isEmpty()) {
//			throw new EmptyShoppingCartException(id);
//		}
//		
//		HashMap<String, Integer> visitedProducts = new HashMap<>();
//		for (Product product : shoppingCart.getProducts()) {
//			if (!visitedProducts.containsKey(product.getName())) {
//				Integer productQuantity = shoppingCart.getQuantityOfProduct(product);
//				if (productQuantity > product.getStock()) {
//					throw new StockException(product);
//				}
//				
//				visitedProducts.put(product.getName(), productQuantity);
//				product.setStock(productQuantity);
//				productRepository.save(product);
//			}
//		}
//		
//		
//	}
	
	@Transactional
	public ShoppingCartResponseDTO addProduct(Integer id, ProductQuantityRequestDTO request) throws NotFoundException, DeletedEntityException, StockException {
		Optional<ShoppingCart> optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new NotFoundException("ShoppingCart", id);
		}
		
		Optional<Product> productOptional = productRepository.findById(request.getProductId());
		if (!productOptional.isPresent()) {
			throw new NotFoundException("Product", request.getProductId());
		}
		
		ShoppingCart shoppingCart = optional.get();
		Product product = productOptional.get();
		
		if (product.getIsDeleted()) {
			throw new DeletedEntityException("Product", product.getName());
		}
		
		if (shoppingCart.getQuantityOfProduct(product) + request.getQuantity() > product.getStock()) {
			throw new StockException(product);
		}
		
		UserDTO user = null;
		try {
			user = webClientBuilder.build()
					.get()
					.uri("http://localhost:8001/api/user/" + shoppingCart.getUserId())
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
	public ShoppingCartResponseDTO removeProduct(Integer id, Integer productId) throws NotFoundException {
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
		
		UserDTO user = null;
		try {
			user = webClientBuilder.build()
					.get()
					.uri("http://localhost:8001/api/user/" + shoppingCart.getUserId())
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
	public ShoppingCartResponseDTO deleteShoppingCart(Integer id) throws NotFoundException {
		Optional<ShoppingCart> optional = repository.findById(id);
		if (optional.isPresent()) {
			ShoppingCart shoppingCart = optional.get();
			
			UserDTO user = null;
			try {
				user = webClientBuilder.build()
						.get()
						.uri("http://localhost:8001/api/user/" + shoppingCart.getUserId())
						.retrieve()
						.bodyToMono(UserDTO.class)
						.block();
			} catch (Exception e) {
				throw new NotFoundException("User", shoppingCart.getUserId());
			}
			
			repository.deleteById(id);
			
			return new ShoppingCartResponseDTO(shoppingCart, user);
		} else {
			throw new NotFoundException("ShoppingCart", id);
		}
	}
}

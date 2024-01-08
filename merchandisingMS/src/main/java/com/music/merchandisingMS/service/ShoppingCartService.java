package com.music.merchandisingMS.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.music.merchandisingMS.dto.ProductQuantityRequestDTO;
import com.music.merchandisingMS.dto.ShoppingCartRequestDTO;
import com.music.merchandisingMS.dto.ShoppingCartResponseDTO;
import com.music.merchandisingMS.exception.EntityWithUserIdAlreadyUsedException;
import com.music.merchandisingMS.exception.NotFoundException;
import com.music.merchandisingMS.exception.SomeEntityDoesNotExistException;
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
	
	@Transactional(readOnly = true)
	public List<ShoppingCartResponseDTO> findAll() {
		return repository.findAll()
				.stream()
				.map( ShoppingCartResponseDTO::new ).toList();
	}
	
	@Transactional(readOnly = true)
	public ShoppingCartResponseDTO findById(Integer id) throws NotFoundException {
		Optional<ShoppingCart> optional = repository.findById(id);
		if (optional.isPresent()) {
			ShoppingCart shoppingCart = optional.get();
			return new ShoppingCartResponseDTO(shoppingCart);
		} else {
			throw new NotFoundException("ShoppingCart", id);
		}
	}
	
	@Transactional
	public ShoppingCartResponseDTO saveShoppingCart(ShoppingCartRequestDTO request) throws EntityWithUserIdAlreadyUsedException, SomeEntityDoesNotExistException {
		Optional<ShoppingCart> optional = repository.findByUserId(request.getUserId());
		if (optional.isPresent()) {
			throw new EntityWithUserIdAlreadyUsedException("ShoppingCart", request.getUserId());
		}
		
		List<Product> products = productRepository.findAllByIds(request.getProducts());
		if (products.size() != request.getProducts().size()) {
			throw new SomeEntityDoesNotExistException("products");
		}
		
		ShoppingCart shoppingCart = new ShoppingCart(request, products);
		Double totalprice = productRepository.getPriceOfProducts(products);
		shoppingCart.setTotalPrice(totalprice);
		
		return new ShoppingCartResponseDTO(repository.save(shoppingCart));
	}
	
	@Transactional
	public ShoppingCartResponseDTO addProduct(Integer id, ProductQuantityRequestDTO request) throws NotFoundException {
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
		
		shoppingCart.addProduct(product, request.getQuantity());
		shoppingCart.setTotalPrice(shoppingCart.getTotalPrice() + product.getPrice() * request.getQuantity());
		
		return new ShoppingCartResponseDTO(repository.save(shoppingCart));
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
		
		Integer removedQuantity = shoppingCart.removeProduct(product);
		shoppingCart.setTotalPrice(shoppingCart.getTotalPrice() - product.getPrice() * removedQuantity);
		
		return new ShoppingCartResponseDTO(repository.save(shoppingCart));
	}
	
	@Transactional
	public ShoppingCartResponseDTO deleteShoppingCart(Integer id) throws NotFoundException {
		Optional<ShoppingCart> optional = repository.findById(id);
		if (optional.isPresent()) {
			ShoppingCart shoppingCart = optional.get();
			repository.deleteById(id);
			
			return new ShoppingCartResponseDTO(shoppingCart);
		} else {
			throw new NotFoundException("ShoppingCart", id);
		}
	}
}

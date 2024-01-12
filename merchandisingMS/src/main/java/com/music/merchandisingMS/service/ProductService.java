package com.music.merchandisingMS.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.music.merchandisingMS.dto.ProductRequestDTO;
import com.music.merchandisingMS.dto.ProductResponseDTO;
import com.music.merchandisingMS.exception.DeletedEntityException;
import com.music.merchandisingMS.exception.NameAlreadyUsedException;
import com.music.merchandisingMS.exception.NotFoundException;
import com.music.merchandisingMS.exception.SomeEntityDoesNotExistException;
import com.music.merchandisingMS.model.Product;
import com.music.merchandisingMS.model.ShoppingCart;
import com.music.merchandisingMS.model.Tag;
import com.music.merchandisingMS.repository.ProductRepository;
import com.music.merchandisingMS.repository.ShoppingCartRepository;
import com.music.merchandisingMS.repository.TagRepository;

@Service("productService")
public class ProductService {

	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private TagRepository tagRepository;
	
	@Autowired
	private ShoppingCartRepository shoppingCartRepository;
	
	@Transactional(readOnly = true)
	public List<ProductResponseDTO> findAll() {
		return repository.findAllByNotDeleted()
				.stream()
				.map( ProductResponseDTO::new ).toList();
	}
	
	@Transactional(readOnly = true)
	public List<ProductResponseDTO> findAllDeletedProducts() {
		return repository.findAllByDeleted()
				.stream()
				.map( ProductResponseDTO::new ).toList();
	}
	
	@Transactional(readOnly = true)
	public List<ProductResponseDTO> findAllByTag(String tagName) throws NotFoundException {
		Optional<Tag> tagOptional = tagRepository.findByName(tagName);
		if (tagOptional.isPresent()) {
			Tag tag = tagOptional.get();
			
			return repository.findAllByTag(tag)
					.stream()
					.map( ProductResponseDTO::new )
					.toList();
		} else {
			throw new NotFoundException("Tag", tagName);
		}
	}
	
	@Transactional(readOnly = true)
	public ProductResponseDTO findById(Integer id) throws NotFoundException, DeletedEntityException {
		Optional<Product> optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new NotFoundException("Product", id);
		}
		
		Product product = optional.get();
		if (product.getIsDeleted()) {
			throw new DeletedEntityException("Product", product.getName());
		}
		
		return new ProductResponseDTO(product);
	}
	
	@Transactional
	public ProductResponseDTO saveProduct(ProductRequestDTO request) throws NameAlreadyUsedException, SomeEntityDoesNotExistException {
		Optional<Product> optional = repository.findByName(request.getName());
		if (optional.isPresent()) {
			throw new NameAlreadyUsedException("Product", request.getName());
		}
		
		List<Tag> tags = tagRepository.findAllByIds(request.getTags());
		if (tags.size() != request.getTags().size()) {
			throw new SomeEntityDoesNotExistException("tags");
		}
		
		Product product = new Product(request, tags);
		return new ProductResponseDTO(repository.save(product));
	}
	
	@Transactional
	public ProductResponseDTO updateProduct(Integer id, ProductRequestDTO request) throws NotFoundException, SomeEntityDoesNotExistException, DeletedEntityException {
		Optional<Product> optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new NotFoundException("Product", id);
		}
		
		Product product = optional.get();
		if (product.getIsDeleted()) {
			throw new DeletedEntityException("Product", product.getName());
		}
		
		List<Tag> tags = tagRepository.findAllByIds(request.getTags());
		if (tags.size() != request.getTags().size()) {
			throw new SomeEntityDoesNotExistException("tags");
		}
		
		product.setName(request.getName());
		product.setPrice(request.getPrice());
		product.setStock(request.getStock());
		product.setTags(tags);
		
		return new ProductResponseDTO(repository.save(product));
	}
	
	@Transactional // Logic delete because cannot delete referenced products on Order
	public ProductResponseDTO deleteProduct(Integer id) throws NotFoundException, DeletedEntityException {
		Optional<Product> optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new NotFoundException("Product", id);
		}
		
		Product product = optional.get();
		if (product.getIsDeleted()) {
			throw new DeletedEntityException("Product", product.getName());
		}
		
		List<ShoppingCart> shoppingCarts = shoppingCartRepository.findAllByProduct(product);
		for (ShoppingCart shoppinCart : shoppingCarts) {
			shoppinCart.removeProduct(product);
		}
		
		product.setIsDeleted(true);
		
		return new ProductResponseDTO(repository.save(product));
	}
}

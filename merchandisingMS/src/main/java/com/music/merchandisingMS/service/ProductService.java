package com.music.merchandisingMS.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.music.merchandisingMS.dto.ProductRequestDTO;
import com.music.merchandisingMS.dto.ProductResponseDTO;
import com.music.merchandisingMS.exception.NameAlreadyUsedException;
import com.music.merchandisingMS.exception.NotFoundException;
import com.music.merchandisingMS.exception.SomeEntityDoesNotExistException;
import com.music.merchandisingMS.model.Product;
import com.music.merchandisingMS.model.Tag;
import com.music.merchandisingMS.repository.ProductRepository;
import com.music.merchandisingMS.repository.TagRepository;

@Service("productService")
public class ProductService {

	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private TagRepository tagRepository;
	
	@Transactional(readOnly = true)
	public List<ProductResponseDTO> findAll() {
		return repository.findAll()
				.stream()
				.map( ProductResponseDTO::new ).toList();
	}
	
	@Transactional(readOnly = true)
	public ProductResponseDTO findById(Integer id) throws NotFoundException {
		Optional<Product> optional = repository.findById(id);
		if (optional.isPresent()) {
			Product product = optional.get();
			return new ProductResponseDTO(product);
		} else {
			throw new NotFoundException("Product", id);
		}
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
	public ProductResponseDTO updateProduct(Integer id, ProductRequestDTO request) throws NotFoundException, SomeEntityDoesNotExistException {
		Optional<Product> optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new NotFoundException("Product", id);
		}
		
		List<Tag> tags = tagRepository.findAllByIds(request.getTags());
		if (tags.size() != request.getTags().size()) {
			throw new SomeEntityDoesNotExistException("tags");
		}
		
		Product product = optional.get();
		product.setName(request.getName());
		product.setPrice(request.getPrice());
		product.setStock(request.getStock());
		product.setTags(tags);
		
		return new ProductResponseDTO(repository.save(product));
	}
	
	@Transactional
	public ProductResponseDTO deleteProduct(Integer id) throws NotFoundException {
		Optional<Product> optional = repository.findById(id);
		if (optional.isPresent()) {
			Product product = optional.get();
			repository.deleteById(id);
			
			return new ProductResponseDTO(product);
		} else {
			throw new NotFoundException("Product", id);
		}
	}
}

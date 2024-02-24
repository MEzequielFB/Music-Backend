package com.music.productTest;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.music.merchandisingMS.controller.ProductController;
import com.music.merchandisingMS.dto.ProductRequestDTO;
import com.music.merchandisingMS.dto.ProductResponseDTO;
import com.music.merchandisingMS.dto.TagResponseDTO;
import com.music.merchandisingMS.exception.DeletedEntityException;
import com.music.merchandisingMS.exception.NameAlreadyUsedException;
import com.music.merchandisingMS.exception.NotFoundException;
import com.music.merchandisingMS.exception.SomeEntityDoesNotExistException;
import com.music.merchandisingMS.service.ProductService;

public class ProductControllerTest {

	@Mock
	private ProductService service;
	
	@InjectMocks
	private ProductController controller;
	
	private ProductResponseDTO productResponseMock;
	private ProductResponseDTO productResponseMock2;
	private ProductRequestDTO productRequestMock;
	
	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
		this.productResponseMock = ProductResponseDTO.builder()
				.id(1)
				.name("lamp")
				.price(19.99)
				.stock(3)
				.tags(List.of(TagResponseDTO.builder()
						.id(1)
						.name("house")
						.build()))
				.build();
		this.productResponseMock2 = ProductResponseDTO.builder()
				.id(2)
				.name("music poster")
				.price(10.0)
				.stock(1)
				.tags(List.of(TagResponseDTO.builder()
						.id(1)
						.name("house")
						.build()))
				.build();
		this.productRequestMock = ProductRequestDTO.builder()
				.name("lamp")
				.price(19.99)
				.stock(3)
				.tags(List.of(1))
				.build();
	}
	
	@Test
	public void findAllTest() {
		List<ProductResponseDTO> productsResponseMock = List.of(productResponseMock);
		when(service.findAll()).thenReturn(productsResponseMock);
		
		ResponseEntity<List<ProductResponseDTO>> responseEntity = controller.findAll();
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(productsResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void findByIdTest() throws NotFoundException, DeletedEntityException {
		when(service.findById(1)).thenReturn(productResponseMock);
		
		ResponseEntity<ProductResponseDTO> responseEntity = controller.findById(1);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(productResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void findAllDeletedProducEmptyTest() {
		when(service.findAllDeletedProducts()).thenReturn(List.of());
		
		ResponseEntity<List<ProductResponseDTO>> responseEntity = controller.findAllDeletedProducts();
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(List.of(), responseEntity.getBody());
	}
	
	@Test
	public void findAllByTagTest() throws NotFoundException {
		List<ProductResponseDTO> productsResponseMock = new LinkedList<>();
		productsResponseMock.add(productResponseMock);
		productsResponseMock.add(productResponseMock2);
		
		when(service.findAllByTag("home")).thenReturn(productsResponseMock);
		
		ResponseEntity<List<ProductResponseDTO>> responseEntity = controller.findAllByTag("home");
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(productsResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void saveProductTest() throws NameAlreadyUsedException, SomeEntityDoesNotExistException {
		when(service.saveProduct(productRequestMock)).thenReturn(productResponseMock);
		
		ResponseEntity<ProductResponseDTO> responseEntity = controller.saveProduct(productRequestMock);
		
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals(productResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void updateProductNameUsedTest() throws NotFoundException, SomeEntityDoesNotExistException, DeletedEntityException, NameAlreadyUsedException {
		when(service.updateProduct(2, productRequestMock)).thenThrow(new NameAlreadyUsedException("Product", productRequestMock.getName()));
		
		ResponseEntity<ProductResponseDTO> responseEntity = null;
		try {
			responseEntity = controller.updateProduct(2, productRequestMock);
		} catch (NameAlreadyUsedException e) {
			assertNull(responseEntity);
		} catch (Exception e) {
			fail("Test threw another exception instead NameAlreadyUsedException");
		}
	}
	
	@Test
	public void deleteProductTest() throws NotFoundException, DeletedEntityException {
		when(service.deleteProduct(1)).thenReturn(productResponseMock);
		
		ResponseEntity<ProductResponseDTO> responseEntity = controller.deleteProduct(1);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(productResponseMock, responseEntity.getBody());
	}
}

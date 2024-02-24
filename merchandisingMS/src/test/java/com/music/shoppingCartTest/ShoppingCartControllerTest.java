package com.music.shoppingCartTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.music.merchandisingMS.controller.ShoppingCartController;
import com.music.merchandisingMS.dto.ProductIdDTO;
import com.music.merchandisingMS.dto.ProductQuantityRequestDTO;
import com.music.merchandisingMS.dto.ProductResponseDTO;
import com.music.merchandisingMS.dto.ShoppingCartRequestDTO;
import com.music.merchandisingMS.dto.ShoppingCartResponseDTO;
import com.music.merchandisingMS.dto.TagResponseDTO;
import com.music.merchandisingMS.dto.UserDTO;
import com.music.merchandisingMS.exception.AccountIdRequest;
import com.music.merchandisingMS.exception.AuthorizationException;
import com.music.merchandisingMS.exception.DeletedEntityException;
import com.music.merchandisingMS.exception.EmptyShoppingCartException;
import com.music.merchandisingMS.exception.EntityWithUserIdAlreadyUsedException;
import com.music.merchandisingMS.exception.NotFoundException;
import com.music.merchandisingMS.exception.SomeEntityDoesNotExistException;
import com.music.merchandisingMS.exception.StockException;
import com.music.merchandisingMS.model.Product;
import com.music.merchandisingMS.service.ShoppingCartService;

public class ShoppingCartControllerTest {

	@Mock
	private ShoppingCartService service;
	
	@InjectMocks
	private ShoppingCartController controller;
	
	private ShoppingCartResponseDTO shoppingCartResponseMock;
	private ShoppingCartRequestDTO shoppingCartRequestMock;
	private AccountIdRequest accountIdRequestMock;
	private ProductQuantityRequestDTO productQuantityRequestMock;
	private ProductIdDTO productIdMock;
	private String tokenMock;
	
	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
		this.shoppingCartResponseMock = ShoppingCartResponseDTO.builder()
				.id(1)
				.totalPrice(20.0)
				.user(UserDTO.builder()
						.id(1)
						.username("eze")
						.email("eze@gmail.com")
						.address("street 123")
						.role("USER")
						.build())
				.products(List.of(ProductResponseDTO.builder()
						.id(1)
						.name("note ring")
						.price(20.0)
						.stock(1)
						.tags(List.of(TagResponseDTO.builder()
								.id(1)
								.name("accesory")
								.build()))
						.build()))
				.build();
		this.shoppingCartRequestMock = ShoppingCartRequestDTO.builder()
				.products(List.of(1))
				.build();
		this.accountIdRequestMock = AccountIdRequest.builder()
				.accountId(1)
				.build();
		this.productQuantityRequestMock = ProductQuantityRequestDTO.builder()
				.productId(1)
				.quantity(3)
				.build();
		this.productIdMock = ProductIdDTO.builder()
				.productId(1)
				.build();
		this.tokenMock = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdXBlcmFkbWluQGdtYWlsLmNvbSIsImlkIjoyMywiYXV0aCI6IlNVUEVSX0FETUlOIiwiZXhwIjoxNzA3NTMyNDU1fQ.JWKKgNu9xZs6c0lr7ZeRd2v_FFCyTle7XpbumUMeQXPLZYJ1TfTydpESzImAnbljMgZ-OrKCVTHytbVyl4edbQ";
	}
	
	@Test
	public void findAllTest() {
		List<ShoppingCartResponseDTO> shoppingCartsResponseMock = List.of(shoppingCartResponseMock);
		when(service.findAll(tokenMock)).thenReturn(shoppingCartsResponseMock);
		
		ResponseEntity<List<ShoppingCartResponseDTO>> responseEntity = controller.findAll(tokenMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(shoppingCartsResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void findByIdTest() throws NotFoundException {
		when(service.findById(1, tokenMock)).thenReturn(shoppingCartResponseMock);
		
		ResponseEntity<ShoppingCartResponseDTO> responseEntity = controller.findById(1, tokenMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(shoppingCartResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void saveShoppingCartTest() throws EntityWithUserIdAlreadyUsedException, SomeEntityDoesNotExistException, NotFoundException, StockException, DeletedEntityException, AuthorizationException {
		when(service.saveShoppingCart(shoppingCartRequestMock, tokenMock)).thenReturn(shoppingCartResponseMock);
		
		ResponseEntity<ShoppingCartResponseDTO> responseEntity = controller.saveShoppingCart(shoppingCartRequestMock, tokenMock);
		
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals(shoppingCartResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void buyProductsTest() throws NotFoundException, EmptyShoppingCartException, StockException, AuthorizationException {
		when(service.buyProducts(1, 1, tokenMock)).thenReturn(shoppingCartResponseMock);
		
		ResponseEntity<ShoppingCartResponseDTO> responseEntity = controller.buyProducts(1, accountIdRequestMock, tokenMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(shoppingCartResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void addProductStockExceptionTest() throws NotFoundException, DeletedEntityException, StockException, AuthorizationException {
		when(service.addProduct(1, productQuantityRequestMock, tokenMock)).thenThrow(new StockException(Product.builder().id(1).name("note ring").stock(1).build()));
		
		ResponseEntity<ShoppingCartResponseDTO> responseEntity = null;
		try {
			responseEntity = controller.addProduct(1, productQuantityRequestMock, tokenMock);
		} catch (StockException e) {
			assertNull(responseEntity);
		} catch (Exception e) {
			fail(String.format("Test threw %s instead of StockException", e.getClass().getName()));
		}
	}
	
	@Test
	public void removeProductTest() throws NotFoundException, AuthorizationException {
		when(service.removeProduct(1, 1, tokenMock)).thenReturn(shoppingCartResponseMock);
		
		ResponseEntity<ShoppingCartResponseDTO> responseEntity = controller.removeProduct(1, productIdMock, tokenMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(shoppingCartResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void deleteShoppingCartTest() throws NotFoundException {
		when(service.deleteShoppingCart(1, tokenMock)).thenReturn(shoppingCartResponseMock);
		
		ResponseEntity<ShoppingCartResponseDTO> responseEntity = controller.deleteShoppingCart(1, tokenMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(shoppingCartResponseMock, responseEntity.getBody());
	}
}

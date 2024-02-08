package com.music.merchandisingMS.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.music.merchandisingMS.dto.OrderResponseDTO;
import com.music.merchandisingMS.dto.OrderStatusUpdateDTO;
import com.music.merchandisingMS.dto.UserDTO;
import com.music.merchandisingMS.exception.NotFoundException;
import com.music.merchandisingMS.model.Order;
import com.music.merchandisingMS.model.OrderStatus;
import com.music.merchandisingMS.model.Status;
import com.music.merchandisingMS.repository.OrderRepository;
import com.music.merchandisingMS.repository.StatusRepository;

@Service("orderService")
public class OrderService {

	@Autowired
	private OrderRepository repository;
	
	@Autowired
	private StatusRepository statusRepository;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Transactional(readOnly = true)
	public List<OrderResponseDTO> findAll(String token) {
		return repository.findAll()
				.stream()
				.map(order -> {
					UserDTO user = webClientBuilder.build()
							.get()
							.uri("http://localhost:8001/api/user/" + order.getUserId() + "/evenDeleted")
							.header("Authorization", token)
							.retrieve()
							.bodyToMono(UserDTO.class)
							.block();
			
					OrderResponseDTO responseDTO = new OrderResponseDTO(order, user);
					return responseDTO;
				})
				.toList();
	}
	
	@Transactional(readOnly = true)
	public OrderResponseDTO findById(Integer id, String token) throws NotFoundException {
		Optional<Order> optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new NotFoundException("Order", id);
		}
		
		Order order = optional.get();
		
		UserDTO user = null;
		try {
			user = webClientBuilder.build()
					.get()
					.uri("http://localhost:8001/api/user/" + order.getUserId() + "/evenDeleted")
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(UserDTO.class)
					.block();
		} catch (Exception e) {
			throw new NotFoundException("User", order.getUserId());
		}
		
		return new OrderResponseDTO(order, user);
	}
	
//	@Transactional
//	public OrderResponseDTO saveOrder(OrderRequestDTO request) throws SomeEntityDoesNotExistException, NotFoundException {
//		Optional<Status> statusOptional = statusRepository.findByName(OrderStatus.PENDING);
//		if (!statusOptional.isPresent()) {
//			throw new NotFoundException("Status", OrderStatus.PENDING);
//		}
//		
//		Status status = statusOptional.get();
//		
//		List<Product> products = productRepository.findAllByIds(request.getProducts());
//		if (request.getProducts().size() != products.size()) {
//			throw new SomeEntityDoesNotExistException("products");
//		}
//		
//		UserDTO user = null;
//		try {
//			user = webClientBuilder.build()
//					.get()
//					.uri("http://localhost:8001/api/user/" + request.getUserId())
//					.retrieve()
//					.bodyToMono(UserDTO.class)
//					.block();
//		} catch (Exception e) {
//			throw new NotFoundException("User", request.getUserId());
//		}
//		
//		Double totalPrice = productRepository.getPriceOfProducts(products);
//		Order order = new Order(request, totalPrice, status, products);
//		return new OrderResponseDTO(repository.save(order), user);
//	}
	
	@Transactional
	public OrderResponseDTO updateOrderStatus(Integer id, OrderStatusUpdateDTO request, String token) throws NotFoundException {
		Optional<Order> optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new NotFoundException("Order", id);
		}
		
		Optional<Status> statusOptional = statusRepository.findByName(request.getStatus());
		if (!statusOptional.isPresent()) {
			throw new NotFoundException("Status", request.getStatus());
		}
		
		Status status = statusOptional.get();
		
		Order order = optional.get();
		order.setStatus(status);
		if (status.getName().equalsIgnoreCase(OrderStatus.DELIVERED)) {
			order.setDeliveredDate(new Date(System.currentTimeMillis()));
		} else if (!status.getName().equalsIgnoreCase(OrderStatus.DELIVERED) && order.getDeliveredDate() != null) {
			order.setDeliveredDate(null);
		}
		
		UserDTO user = null;
		try {
			user = webClientBuilder.build()
					.get()
					.uri("http://localhost:8001/api/user/" + order.getUserId() + "/evenDeleted")
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(UserDTO.class)
					.block();
		} catch (Exception e) {
			throw new NotFoundException("User", order.getUserId());
		}
		
		return new OrderResponseDTO(repository.save(order), user);
	}
	
	@Transactional
	public OrderResponseDTO deleteOrder(Integer id, String token) throws NotFoundException {
		Optional<Order> optional = repository.findById(id);
		if (optional.isPresent()) {
			Order order = optional.get();
			repository.deleteById(id);
			
			UserDTO user = null;
			try {
				user = webClientBuilder.build()
						.get()
						.uri("http://localhost:8001/api/user/" + order.getUserId() + "/evenDeleted")
						.header("Authorization", token)
						.retrieve()
						.bodyToMono(UserDTO.class)
						.block();
			} catch (Exception e) {
				throw new NotFoundException("User", order.getUserId());
			}
			
			return new OrderResponseDTO(order, user);
		} else {
			throw new NotFoundException("Order", id);
		}
	}
}

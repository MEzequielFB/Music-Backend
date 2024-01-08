package com.music.merchandisingMS.model;

import java.util.List;

import com.music.merchandisingMS.dto.ShoppingCartRequestDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private Integer userId;
	
	@Column(nullable = false)
	private Double totalPrice;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			joinColumns = {@JoinColumn(name = "shopping_cart_id")},
			inverseJoinColumns = {@JoinColumn(name = "product_id")})
	private List<Product> products;
	
	public ShoppingCart(ShoppingCartRequestDTO request, List<Product> products) {
		this.userId = request.getUserId();
		this.products = products;
	}
	
	public void addProduct(Product product, Integer quantity) {
		for (int i = 0; i < quantity; i++) {
			products.add(product);
		}
	}
	
	public Integer removeProduct(Product product) {
		Integer i = 0;
		while (products.contains(product)) {
			products.remove(product);
			i++;
		}
		return i;
	}
}

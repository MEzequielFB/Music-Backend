package com.music.merchandisingMS.model;

import java.util.Set;

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
	private Set<Product> products;
	
	public ShoppingCart(Integer userId, Set<Product> products) {
		this.userId = userId;
		this.products = products;
	}
	
//	public ShoppingCart(ShoppingCartRequestDTO request, List<Product> products) {
//		this.userId = request.getUserId();
//		this.products = products;
//	}
	
	public void addProduct(Product product, Integer quantity) {
		Double totalPlusPrice = 0.0;
		
		for (int i = 0; i < quantity; i++) {
			products.add(product);
			totalPlusPrice += product.getPrice() * (1.0 - (product.getDiscount() / 100));
		}
		
		setTotalPrice(Math.round((totalPrice + totalPlusPrice) * 100.0) / 100.0);
	}
	
	public Integer removeProduct(Product product) {
		Integer i = 0;
		Double totalMinusPrice = 0.0;
		
		while (products.contains(product)) {
			products.remove(product);
			
			totalMinusPrice += product.getPrice() * (1.0 - (product.getDiscount() / 100));
			i++;
		}
		
		setTotalPrice(Math.round((totalPrice - totalMinusPrice) * 100.0) / 100.0);
		return i;
	}
	
	public Integer getQuantityOfProduct(Product product) {
		Integer quantity = 0;
		for (Product actualProduct : products) {
			if (actualProduct.equals(product)) {
				quantity++;
			}
		}
		return quantity;
	}
}

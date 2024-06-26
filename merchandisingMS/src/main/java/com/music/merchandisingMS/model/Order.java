package com.music.merchandisingMS.model;

import java.util.Date;
import java.util.Set;

import com.music.merchandisingMS.dto.OrderRequestDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_table") // Not 'order' because is a reserved keyword in most SQL databases, including MariaDB
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private Integer userId;
	
	@Column(nullable = false)
	private Date createdAt;
	
	@Column(nullable = false)
	private String shippingAddress;
	
	@Column
	private Date deliveredDate;
	
	@Column(nullable = false)
	private Double totalPrice;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Status status;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			joinColumns = {@JoinColumn(name = "order_id")},
			inverseJoinColumns = {@JoinColumn(name = "product_id")})
	private Set<Product> products;
	
	public Order(Integer userId, String shippingAddress, Double totalPrice, Status status, Set<Product> products) {
		this.userId = userId;
		this.createdAt = new Date(System.currentTimeMillis());
		this.shippingAddress = shippingAddress;
		this.deliveredDate = null;
		this.totalPrice = totalPrice;
		this.status = status;
		this.products = products;
	}
	
	public Order(OrderRequestDTO request, Double totalPrice, Status status, Set<Product> products) {
		this.userId = request.getUserId();
		this.createdAt = new Date(System.currentTimeMillis());
		this.shippingAddress = request.getShippingAddress();
		this.deliveredDate = null;
		this.totalPrice = totalPrice;
		this.status = status;
		this.products = products;
	}
}

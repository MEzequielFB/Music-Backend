package com.music.merchandisingMS.model;

import java.util.Date;
import java.util.List;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
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
	private Date date;
	
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
	private List<Product> products;
}

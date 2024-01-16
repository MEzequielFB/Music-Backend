package com.music.merchandisingMS.model;

import java.util.List;

import com.music.merchandisingMS.dto.ProductRequestDTO;

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
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private Double price;
	
	@Column
	private Integer discount;
	
	@Column(nullable = false)
	private Integer stock;
	
	@Column(nullable = false)
	private Boolean isDeleted;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			joinColumns = {@JoinColumn(name = "product_id")},
			inverseJoinColumns = {@JoinColumn(name = "tag_id")})
	private List<Tag> tags;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "products")
	private List<Order> orders;
	
	public Product(ProductRequestDTO request, List<Tag> tags) {
		this.name = request.getName();
		this.price = request.getPrice();
		this.discount = 0;
		this.stock = request.getStock();
		this.isDeleted = false;
		this.tags = tags;
	}
	
	public void removeTag(Tag tag) {
		tags.remove(tag);
	}
}

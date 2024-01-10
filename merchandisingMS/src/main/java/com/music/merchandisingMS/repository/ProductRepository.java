package com.music.merchandisingMS.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.music.merchandisingMS.model.Product;

@Repository("productRepository")
public interface ProductRepository extends JpaRepository<Product, Integer> {

	@Query("SELECT SUM(p.price)"
			+ " FROM Product p"
			+ " WHERE p IN :products")
	public Double getPriceOfProducts(List<Product> products);
	
	@Query("SELECT p"
			+ " FROM Product p"
			+ " WHERE p.id IN :ids")
	public List<Product> findAllByIds(List<Integer> ids);
	
	public Optional<Product> findByName(String name);
}

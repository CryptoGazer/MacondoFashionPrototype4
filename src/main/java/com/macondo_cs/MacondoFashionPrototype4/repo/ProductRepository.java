package com.macondo_cs.MacondoFashionPrototype4.repo;

import com.macondo_cs.MacondoFashionPrototype4.models.Image;
import com.macondo_cs.MacondoFashionPrototype4.models.Product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>{
    List<Product> findByCategory(String category);
    Product findByProductId(Long productId);
    List<Product> findByName(String name);
    List<Product> findByImage(Image image);
}

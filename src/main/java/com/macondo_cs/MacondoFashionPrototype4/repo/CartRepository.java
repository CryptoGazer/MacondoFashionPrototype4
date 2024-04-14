package com.macondo_cs.MacondoFashionPrototype4.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.macondo_cs.MacondoFashionPrototype4.models.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUserId(Long userId);
}

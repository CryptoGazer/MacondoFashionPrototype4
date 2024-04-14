package com.macondo_cs.MacondoFashionPrototype4.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.macondo_cs.MacondoFashionPrototype4.models.*;

public interface ImageRepository extends JpaRepository<Image, Long> {
    void deleteByProduct_productId(Long productId);
    Image findByProduct_productId(Long product_productId);
}

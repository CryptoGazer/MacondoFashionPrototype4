package com.macondo_cs.MacondoFashionPrototype4.services;

import java.io.IOException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.macondo_cs.MacondoFashionPrototype4.models.*;
import com.macondo_cs.MacondoFashionPrototype4.repo.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final CartRepository cartRepository;
    
    @Autowired
    private final ImageRepository imageRepository;

    public List<Product> listAllProducts() {
        List<Product> products = productRepository.findAll();
        if (!products.isEmpty()) {
            return products;
        }
        return List.of();
    }

    public List<Product> listProductsByName(String name) {
        List<Product> products = productRepository.findByName(name);
        if (name != null) {
            return productRepository.findByName(name);
        }
        return products;
    }

    public void saveProduct(Product product, MultipartFile file) throws IOException {
        Image image;
        if (file.getSize() != 0) {
            image = toImageEntity(file);
            product.addImageToProduct(image);
        }

        log.info("Saving new Product. Name: {};", product.getName());
        productRepository.save(product);
    }

    public void saveProduct(Long id, MultipartFile file) throws IOException {
        Image image;
        Product product = getProductById(id);
        if (file.getSize() != 0) {
            image = toImageEntity(file);
            product.addImageToProduct(image);
        }

        log.info("Saving new Product. Name: {};", product.getName());
        productRepository.save(product);
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());

        return image;
    }

    public void updateProduct(Product product, MultipartFile file, Long existingProductId) throws IOException {
        // log.info("\n\nCurrent ID: {}\n\n", product.getProductId());
        // log.info("\n\nCurrent Name: {}\n\n", product.getName());

        Product existingProduct = productRepository.findByProductId(existingProductId);
        Double existingPrice = existingProduct.getPrice();
        Integer existingQuantity = existingProduct.getQuantity();
        Integer existingTotalSold = existingProduct.getTotalSold();
        Image existingImage = imageRepository.findByProduct_productId(existingProductId);
        // Long existingProductId = existingProduct.getProductId();
        log.info("\n\nCHANGING: {}\n\n", existingProduct.getPrice());

        if (existingProduct != null && (existingPrice != null && existingQuantity != null && existingTotalSold != null)) {
            log.info("\n\nCHANGING: {}\n\n", product.getName());

            existingProduct.setName(product.getName());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setSex(product.getSex());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setQuantity(product.getQuantity());
            existingProduct.setTotalSold(product.getTotalSold());

            // Product savedProduct = productRepository.save(existingProduct);

            log.info("\n\nsavedProduct: {}\n\n", existingProduct);

            // Image image = imageRepository.findByProduct_productId(existingProductId);

            // image = toImageEntity(file);
            // image.setProduct(savedProduct);

            // if (image == null) {
            //     image = toImageEntity(file);
            //     image.setProduct(savedProduct);
            // }
            
            log.info("\n\ncurrent file: {}\n\n", file);

            if (file.getSize() != 0) {
                existingImage.setName(file.getName());
                existingImage.setOriginalFileName(file.getOriginalFilename());
                existingImage.setContentType(file.getContentType());
                existingImage.setSize(file.getSize());
                existingImage.setBytes(file.getBytes());

                existingProduct.addImageToProduct(existingImage);
            }
            
            productRepository.save(existingProduct);
        }
    }

    @Transactional
    public void deleteProduct(Long id) {
        imageRepository.deleteByProduct_productId(id);
        productRepository.deleteById(id);
    }

    @Transactional
    public void purchasingProcess(Long existingProductId, int quantity, Long userId) {
        log.info("\n\nInp quantity: {}\n\n", quantity);

        // Cart functionality
        if (cartRepository.findByUserId(userId) == null) {
            Cart cart = toCartEntity(existingProductId, userId, quantity);
            cartRepository.save(cart);
        } else {
            List<Cart> cartsByUser = cartRepository.findByUserId(userId);
            boolean foundProduct = false;
            Cart existingCart = null;
            for (Cart subCart : cartsByUser) {
                Product subCartProduct = productRepository.findByProductId(subCart.getProductId());
                if (subCartProduct != null && subCartProduct.getProductId().equals(existingProductId) && !subCart.getIsFinished()) {
                    foundProduct = true;
                    existingCart = subCart;
                    break;
                } 
            }
            if (foundProduct) {
                if (existingCart != null ) {
                    existingCart.setQuantity(existingCart.getQuantity() + quantity);
                    existingCart.setSum(existingCart.getSum() + existingCart.getProductPrice() * existingCart.getQuantity());
                    cartRepository.save(existingCart);
                } else {
                    log.info("\nNot Existing Cart Exception\n!");
                }
            } else {
                Cart cart = toCartEntity(existingProductId, userId, quantity);
                cartRepository.save(cart);
            }
        }

        Product existingProduct = productRepository.findByProductId(existingProductId);
        if (existingProduct != null) {
            existingProduct.setQuantity(existingProduct.getQuantity() - quantity);
            productRepository.save(existingProduct);
        }

    }

    @Transactional
    public void updateProductAfterCartRemoval(Long existingProductId, int quantityToReturn) {
        Product existingProduct = productRepository.findByProductId(existingProductId);
        if (existingProduct != null) {
            existingProduct.setQuantity(existingProduct.getQuantity() + quantityToReturn);
            productRepository.save(existingProduct);
        }
    }

    @Transactional
    public void saveCart(Cart existingCart, User existingUser) {
        existingUser.setTotalBought(existingUser.getTotalBought() + existingCart.getQuantity());
        existingUser.setTotalSpent(existingUser.getTotalSpent() + existingCart.getSum());
        existingCart.setIsFinished(true);

        cartRepository.save(existingCart);
        userRepository.save(existingUser);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    private Cart toCartEntity(Long productId, Long userId, int quantity) {
        Cart cart = new Cart();
        Product product = productRepository.findByProductId(productId);
        User user = userRepository.findById(userId).get();
        double price = product.getPrice();

        log.info(
            "\nProduct: {}\nUser: {}\nPrice: {}\n",
            product, user, price
            );

        if (user != null) {
            cart.setUserName(user.getName());
            cart.setProductName(product.getName());
            cart.setQuantity(quantity);
            cart.setProductPrice(price);
            cart.setSum(price * quantity);
            cart.setIsFinished(false);
            cart.setUserId(userId);
            cart.setProductId(productId);
            // cart.setUser(List.of(user));
        }
        return cart;
    }
}

package com.macondo_cs.MacondoFashionPrototype4.controllers;

import com.macondo_cs.MacondoFashionPrototype4.models.*;
import com.macondo_cs.MacondoFashionPrototype4.repo.*;
import com.macondo_cs.MacondoFashionPrototype4.services.*;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class DatabasesController {
    @Autowired
    private final ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/admin/databases", method = RequestMethod.GET)
    public ModelAndView databasePage() {
        ModelAndView modelAndView = new ModelAndView("databases/databases");
        modelAndView.addObject("title", "databases");
        modelAndView.addObject("btnLeftCap", "Products Database");
        modelAndView.addObject("btnRightCap", "Users Database");
        modelAndView.addObject("btnLeft", "admin/databases/products-database");
        modelAndView.addObject("btnRight", "admin/databases/users-database");

        return modelAndView;
    }

    @RequestMapping(value = "/admin/databases/{databaseType}", method = RequestMethod.GET)
    public ModelAndView databasePageGet(@PathVariable(value = "databaseType") String databaseType) {
        ModelAndView modelAndView = new ModelAndView(String.format("databases/%s", databaseType));
        
        modelAndView.addObject("title", databaseType);
        modelAndView.addObject("productInfoStyle", "none");
        return modelAndView;
    }

    @RequestMapping(value = "/admin/databases/products-database", method = RequestMethod.POST)
    public ModelAndView databasePagePost(
                                    @RequestParam String name,
                                    @RequestParam Double price,
                                    @RequestParam String category,
                                    @RequestParam String description,
                                    @RequestParam int quantity,
                                    @RequestParam("file") MultipartFile file,
                                    Product product
                                    ) throws IOException {
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/databases/products-database");
        modelAndView.addObject("title", "products-database");
        modelAndView.addObject("productInfoStyle", "none");
        
        productService.saveProduct(product, file);
        
        return modelAndView;
    }

    @RequestMapping(value = "/admin/databases/products-database/products", method = RequestMethod.GET)
    public ModelAndView productsInfoPage() {
        Iterable<Product> products = productRepository.findAll();
        ModelAndView modelAndView = new ModelAndView("databases/products");
        
        modelAndView.addObject("title", "products");
        modelAndView.addObject("productsTableProps", ServiceFunctionality.productsTableProps);
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    // edit page get
    @RequestMapping(value = "/admin/databases/products-database/edit/{productID}", method = RequestMethod.GET)
    public ModelAndView productsEditPageGet(@PathVariable(value = "productID") Long productID) {
        if (!productRepository.existsById(productID)) {
            return new ModelAndView("redirect:/admin/databases/products-database");
        }

        ModelAndView modelAndView = new ModelAndView("databases/edit-products-database");
        Optional<Product> productOptional = productRepository.findById(productID);
        ArrayList<Product> product = new ArrayList<>();
        productOptional.ifPresent(product::add);

        modelAndView.addObject("title", String.format("product-details-%s", productID));
        modelAndView.addObject("productID", productID);
        modelAndView.addObject("product", product.get(0));

        return modelAndView;
    }

    // edit page post
    @RequestMapping(value = "/admin/databases/products-database/edit/{productID}", method = RequestMethod.POST)
    public ModelAndView productsEditPagePost(
        @PathVariable(value = "productID") Long productID,
        @RequestParam String name,
        @RequestParam(required = false) Double price,
        @RequestParam String category,
        @RequestParam String description,
        @RequestParam(required = false) Integer quantity,
        @RequestParam(required = false) Integer totalSold,
        @RequestParam("file") MultipartFile file,
        Product product
        // Product product
        ) throws IOException {
            ModelAndView modelAndView = new ModelAndView("redirect:/admin/databases/products-database");
            // Product product = productRepository.findById(productID).orElseThrow();
            
            // if (price != null && quantity != null && totalSold != null) {
            //     product.setName(name);
            //     product.setPrice(price);
            //     product.setCategory(category);
            //     product.setDescription(description);
            //     product.setQuantity(quantity);
            //     product.setTotalSold(totalSold);

            //     productService.saveProduct(product, file);
            //     // productRepository.save(product);
            // }
            productService.updateProduct(product, file, productID);
            return modelAndView;
        }

    @RequestMapping(value = "/admin/databases/products-database/{productID}", method = RequestMethod.GET)
    public ModelAndView displayProductByID(@PathVariable(value = "productID") Long productID) {
        if (!productRepository.existsById(productID)) {
            return new ModelAndView("redirect:/admin/databases/products-database");
        }

        ModelAndView modelAndView = new ModelAndView("databases/product-details");
        Optional<Product> productOptional = productRepository.findById(productID);
        ArrayList<Product> product = new ArrayList<>();
        productOptional.ifPresent(product::add);

        modelAndView.addObject("title", String.format("product-details-%s", productID));
        modelAndView.addObject("productID", productID);
        modelAndView.addObject("product", product.get(0));

        return modelAndView;
    }

    @RequestMapping(value = "/admin/databases/products-database/{productID}/remove", method = RequestMethod.POST)
    public ModelAndView removeFromDatabase(@PathVariable(value = "productID") Long productID) {
        // Product product = productRepository.findById(productID).orElseThrow();
        // productRepository.delete(product);
        
        productService.deleteProduct(productID);
        return new ModelAndView("redirect:/admin/databases/products-database");
    }

    // USERS

    @RequestMapping(value = "/admin/databases/users-database/users", method = RequestMethod.GET)
    public ModelAndView usersInfoPage() {
        Iterable<User> users = userRepository.findAll();
        ModelAndView modelAndView = new ModelAndView("databases/users");
        
        modelAndView.addObject("title", "users");
        modelAndView.addObject("usersTableProps", ServiceFunctionality.usersTableProps);
        modelAndView.addObject("users", users);
        return modelAndView;
    }

    @RequestMapping(value = "/admin/databases/users-database/{userID}", method = RequestMethod.GET)
    public ModelAndView displayUserByID(@PathVariable(value = "userID") Long userID) {
        if (!userRepository.existsById(userID)) {
            return new ModelAndView("redirect:/admin/databases/users-database");
        }

        ModelAndView modelAndView = new ModelAndView("databases/user-details");
        Optional<User> userOptional = userRepository.findById(userID);
        ArrayList<User> user = new ArrayList<>();
        userOptional.ifPresent(user::add);

        modelAndView.addObject("title", String.format("user-details-%s", userID));
        modelAndView.addObject("userID", userID);
        modelAndView.addObject("user", user.get(0));

        return modelAndView;
    }

    @RequestMapping(value = "/admin/list-of-orders", method = RequestMethod.GET)
    public ModelAndView listOfOrders() {
        ModelAndView modelAndView = new ModelAndView("databases/list-of-orders");
        
        modelAndView.addObject("title", "list-of-orders");
        return modelAndView;
    }
}



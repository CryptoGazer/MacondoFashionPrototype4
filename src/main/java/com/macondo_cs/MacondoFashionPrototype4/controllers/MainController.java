package com.macondo_cs.MacondoFashionPrototype4.controllers;

import com.macondo_cs.MacondoFashionPrototype4.models.*;
import com.macondo_cs.MacondoFashionPrototype4.repo.*;
import com.macondo_cs.MacondoFashionPrototype4.services.ProductService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class MainController {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductService productService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView mainPage(Model model) {
        String[] pages = {"men", "women"};
        ModelAndView modelAndView = new ModelAndView("index");

        modelAndView.addObject("title", "main");
        modelAndView.addObject("btnLeft", pages[0]);
        modelAndView.addObject("btnRight", pages[1]);
        modelAndView.addObject("btnLeftCap", StringUtils.capitalize(pages[0]));
        modelAndView.addObject("btnRightCap", StringUtils.capitalize(pages[1]));
//        modelAndView.addObject("pages", pages);
        return modelAndView;
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public ModelAndView errorPage() {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("title", "error");
        return modelAndView;
    }

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public ModelAndView aboutPage() {
        ModelAndView modelAndView = new ModelAndView("about");
        modelAndView.addObject("title", "aboutPage");
        return modelAndView;
    }

    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public ModelAndView contactPage() {
        ModelAndView modelAndView = new ModelAndView("contact");
        modelAndView.addObject("title", "contactPage");
        return modelAndView;
    }

    // men/women choice
    @RequestMapping(value = "/{sex}", method = RequestMethod.GET)
    public ModelAndView sexPage(@PathVariable(value = "sex") String sex) {
        String[] productTypes = {"clothes", "accessories"};
        ModelAndView modelAndView = new ModelAndView(sex);

        modelAndView.addObject("title", sex);
        modelAndView.addObject("btnLeft", String.format("%s/%s", sex, productTypes[0]));
        modelAndView.addObject("btnRight", String.format("%s/%s", sex, productTypes[1]));
        modelAndView.addObject("btnLeftCap", StringUtils.capitalize(productTypes[0]));
        modelAndView.addObject("btnRightCap", StringUtils.capitalize(productTypes[1]));
        modelAndView.addObject("clothesNames", ServiceFunctionality.clothesNames);
        modelAndView.addObject("accessoriesNames", ServiceFunctionality.accessoriesNames);
//        modelAndView.addObject("pages", sexes);
        return modelAndView;
    }

    @RequestMapping(value = "/{sex}/{productName}", method = RequestMethod.GET)
    public ModelAndView clothesChoice(
        @PathVariable(value = "sex") String sex,
        @PathVariable(value = "productName") String productName) {
        String productType = ServiceFunctionality.getProductType(productName);
        if (!productType.isEmpty()) {
            String productCategoryForDBQuery;
            if (productName.equals("coats-jackets")) {
                productCategoryForDBQuery = "Coats & Jackets";
            } else if (productName.equals("t-shirt")) {
                productCategoryForDBQuery = "T-Shirt";
            } else {
                productCategoryForDBQuery = StringUtils.capitalize(productName);
            }

            int intSex = sex.equals("men") ? 1 : 0;
            log.info("\nINTSEX: {}\n", intSex);
            List<Product> products = productRepository.findByCategory(productCategoryForDBQuery);
            // Product product = products.get(0);
            // boolean isPresent = (product.getQuantity() > 0) ? true : false;
            ArrayList<ArrayList<Product>> productsTable = new ArrayList<ArrayList<Product>>();
            int j = 0;

            for (int i = 0; i < Math.ceil(products.size() / 6.0); i++) {
                int k = 0;
                productsTable.add(new ArrayList<Product>());
                while (j < products.size() && k < 6) {
                    log.info("\nProductsSex: {}\n{}\n\n", products.get(j).getSex(), products.toString());
                    if (products.get(j).getSex() == intSex) {
                        productsTable.get(i).add(products.get(j));
                    }
                    j++;
                    k++;
                }
            }

            ModelAndView modelAndView = new ModelAndView(String.format("%s/%s", productType, productName.toLowerCase()));
            modelAndView.addObject("title", String.format("%s-%s", sex, productName));
            modelAndView.addObject("sex", sex);
            if (productName.equals("Coats & Jackets")) {
                productName = "coats-jackets";
            }
            modelAndView.addObject("productName", productName);
            modelAndView.addObject("clothesNames", ServiceFunctionality.clothesNames);
            modelAndView.addObject("accessoriesNames", ServiceFunctionality.accessoriesNames);

            modelAndView.addObject("products", products);
            modelAndView.addObject("productsTable", productsTable);
            return modelAndView;
        }
        return new ModelAndView("error");
    }

    @RequestMapping(value = "/{sex}/{productName}/{productId}", method = RequestMethod.GET)
    public ModelAndView getClothesById(
        @PathVariable(value = "sex") String sex,
        @PathVariable(value = "productName") String productName,
        @PathVariable(value = "productId") Long productId) {

        String productType = ServiceFunctionality.getProductType(productName);
        if (!productType.isEmpty()) {
            Product product = productRepository.findByProductId(productId);
            ModelAndView modelAndView = new ModelAndView("clothes-by-id");
            modelAndView.addObject("title", String.format("%s-%s-%s", sex, productName, productId));
            modelAndView.addObject("productId", productId);
            modelAndView.addObject("sex", sex);
            modelAndView.addObject("linkProductCategory", productName);
            modelAndView.addObject("clothesNames", ServiceFunctionality.clothesNames);
            modelAndView.addObject("accessoriesNames", ServiceFunctionality.accessoriesNames);
            modelAndView.addObject("name", product.getName());
            modelAndView.addObject("price", product.getPrice());
            modelAndView.addObject("productQuantity", product.getQuantity());
            modelAndView.addObject("image", product.getImage());
            return modelAndView;
        }
        return new ModelAndView("error");
    }

    // @ModelAttribute
    // public void addAttributes(ModelAndView modelAndView, Principal principal) {
    //     boolean isAuthorized;
    //     if (principal != null && principal instanceof UserDetails) {
    //         Collection<? extends GrantedAuthority> authorities = ((UserDetails) principal).getAuthorities();
    //         isAuthorized = !authorities.isEmpty(); 
    //     } else {
    //         isAuthorized = false;
    //     }

    //     modelAndView.addObject("username", principal != null ? principal.getName() : "");
    //     modelAndView.addObject("authorized", isAuthorized);
    // }

    // private boolean isAuthorized(Principal principal) {
    //     log.info("\nPrincipal: {} {}\n", principal, principal.getName());
    //     if (principal != null && principal instanceof UserDetails) {
    //         Collection<? extends GrantedAuthority> authorities = ((UserDetails) principal).getAuthorities();
    //         return !authorities.isEmpty(); 
    //     }
    //     return false;
    // }
}

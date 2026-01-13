package com.macondo_cs.MacondoFashionPrototype4.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.macondo_cs.MacondoFashionPrototype4.models.*;
import com.macondo_cs.MacondoFashionPrototype4.repo.*;
import com.macondo_cs.MacondoFashionPrototype4.services.*;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class PurchasingProcessController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/{sex}/{productName}/{productId}/buy", method = RequestMethod.GET)
    public ModelAndView getBuyProduct(
        @PathVariable(value = "sex") String sex,
        @PathVariable(value = "productName") String productName,
        @PathVariable(value = "productId") Long productId
        ) {
            ModelAndView modelAndView = new ModelAndView("buyProduct");
            Product product = productRepository.findByProductId(productId);

            modelAndView.addObject("title", String.format("%s-buy", productName));
            modelAndView.addObject("product", product);
            return modelAndView;
        }
    
    @RequestMapping(value = "/{sexPath}/{productName}/{productId}/add-to-cart", method = RequestMethod.POST)
    public ModelAndView postClothesById(
        @PathVariable(value = "sexPath") String sexPath,
        @PathVariable(value = "productName") String productName,
        @PathVariable(value = "productId") Long productId,
        @RequestParam(name = "quantityInp") int quantityInp,
        Authentication authentication,
        Product product
        ) throws IOException {
            log.info("\nQuantity: {}\n", quantityInp);
            String userName = authentication.getName();
            User currentUser = userRepository.findByName(userName).get();
            Long currentUserId = currentUser.getUserId();

            log.info("\nUserName: {}\nCurrentUser: {}\nCurrentUserId: {}\n", userName, currentUser, currentUserId);

            ModelAndView modelAndView = new ModelAndView(String.format("redirect:/%s/%s/%s", sexPath, productName, productId));
            productService.purchasingProcess(productId, quantityInp, currentUserId);
            return modelAndView;
    }

    @RequestMapping(value = "/cart/{userName}", method=RequestMethod.GET)
    public ModelAndView getCartPage(
        @PathVariable(value = "userName") String userName
    ) {
        ModelAndView modelAndView = new ModelAndView("cart");
        Long userId = userRepository.findByName(userName).get().getUserId();
        List<Cart> cart = cartRepository.findByUserId(userId);
        List<Cart> currentCart = new ArrayList<>();
        for (Cart subCart : cart) {
            if (!subCart.getIsFinished()) {
                currentCart.add(subCart);
            }
        }
        boolean cartIsEmpty = currentCart.isEmpty();

        modelAndView.addObject("title", String.format("%s-cart", userName));
        modelAndView.addObject("cart", currentCart);
        modelAndView.addObject("isEmpty", cartIsEmpty);
        modelAndView.addObject("cartHeaders", ServiceFunctionality.cartHeaders);

        return modelAndView;
    }

    @RequestMapping(value = "/cart/{userName}/clearCart", method=RequestMethod.POST)
    public ModelAndView postCartPage(
        @PathVariable(value = "userName") String userName
    ) {
        ModelAndView modelAndView = new ModelAndView("redirect:/");
        Long userId = userRepository.findByName(userName).get().getUserId();
        List<Cart> cart = cartRepository.findByUserId(userId);

        for (Cart subCart : cart) {
            Product productInSubCart = productService.getProductById(subCart.getProductId());
            productService.updateProductAfterCartRemoval(
                productInSubCart.getProductId(),
                subCart.getQuantity()
                );
            cartRepository.delete(subCart);
        }

        return modelAndView;
    }

    @RequestMapping(value = "/buy/{userName}", method=RequestMethod.GET)
    public ModelAndView getBuyPage(
        @PathVariable(value = "userName") String userName
    ) {
        ModelAndView modelAndView = new ModelAndView("buy");
        modelAndView.addObject("title", String.format("%s-buy", userName));
        return modelAndView;
    }
    
    @RequestMapping(value = "/buy/{userName}", method=RequestMethod.POST)
    public ModelAndView postBuyPAge(
        @PathVariable(value = "userName") String userName
    ) {
        ModelAndView modelAndView = new ModelAndView("redirect:/");
        Long userId = userRepository.findByName(userName).get().getUserId();
        List<Cart> cart = cartRepository.findByUserId(userId);
        User user = userRepository.findById(userId).get();
        for (Cart subCart : cart) {
            productService.saveCart(subCart, user);
        }

        return modelAndView;
    }

    // @RequestMapping(value = "/{sex}/{productName}/{productId}/buy", method = RequestMethod.GET)
    // public ModelAndView postBuyProduct(
    //     @PathVariable(value = "sex") String sex,
    //     @PathVariable(value = "productName") String productName,
    //     @PathVariable(value = "productId") Long productId
    //     ) {
    //         ModelAndView modelAndView = new ModelAndView("redirect:/{sex}/{productName}/{productId}/buy/pay");
    //         Product product = productRepository.findByProductId(productId);

    //         modelAndView.addObject("product", product);
    //         return modelAndView;
    //     }
}

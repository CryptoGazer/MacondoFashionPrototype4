package com.macondo_cs.MacondoFashionPrototype4.controllers;

import com.macondo_cs.MacondoFashionPrototype4.models.ServiceFunctionality;
import com.macondo_cs.MacondoFashionPrototype4.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController {
    @Autowired
    ProductRepository productRepository;

    // Test of admin functionality. I'll add security afterwards

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public ModelAndView adminPage() {
        ModelAndView modelAndView = new ModelAndView("admin/admin");
        modelAndView.addObject("title", "admin");
        return modelAndView;
    }

    @RequestMapping(value = "/admin/categories", method = RequestMethod.GET)
    public ModelAndView categoriesPage() {
        ModelAndView modelAndView = new ModelAndView("admin/categories");
        modelAndView.addObject("title", "categories");
        modelAndView.addObject("categories", ServiceFunctionality.getCategoryToSubcategory());
        return modelAndView;
    }

    @RequestMapping(value = "/admin/ways-of-payment", method = RequestMethod.GET)
    public ModelAndView waysOfPaymentPage() {
        ModelAndView modelAndView = new ModelAndView("admin/ways-of-payment");
        modelAndView.addObject("title", "ways-of-payment");
        return modelAndView;
    }
}

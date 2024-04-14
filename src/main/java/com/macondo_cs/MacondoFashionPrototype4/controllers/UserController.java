package com.macondo_cs.MacondoFashionPrototype4.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.macondo_cs.MacondoFashionPrototype4.models.*;
import com.macondo_cs.MacondoFashionPrototype4.services.UserService;


@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", method=RequestMethod.GET)
    public ModelAndView getRegisterPage() {
        NewUserDTO user = new NewUserDTO();
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("title", "register");
        
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/register/save", method=RequestMethod.POST)
    public ModelAndView registration(User newUser,
                               BindingResult result
                               ){
        // User existingUser = userService.findByName(newUser.getName());
        ModelAndView modelAndView = new ModelAndView("redirect:/login");

        // if(existingUser != null && !existingUser.getName().isEmpty()){
        //     result.rejectValue("name", null,
        //             "There is already an account registered with the same name!");
        // }

        // if(result.hasErrors()){
        //     ModelAndView modelAndView2 = new ModelAndView();
        //     modelAndView2.addObject("user", newUser);
        //     return modelAndView2;
        // }

        userService.registerUser(newUser);
        return modelAndView;
    }

    @RequestMapping(value = "/login", method=RequestMethod.GET)
    public ModelAndView getLogin(){
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("title", "login");
        return modelAndView;
    }

    @RequestMapping(value = "/login", method=RequestMethod.POST)
    public ModelAndView postLogin(){
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/logout", method=RequestMethod.GET)
    public String logoutRedirect(){
        return "redirect:/";
    }

    @RequestMapping(value = "/profile/{userName}", method=RequestMethod.GET)
    public ModelAndView getUserProfile(
        @PathVariable String userName,
        Authentication authentication
    ) {
        ModelAndView modelAndView = new ModelAndView("account");
        modelAndView.addObject("title", String.format("profile-%s", userName));
        User user = userService.findByName(userName);

        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/profile/{userName}", method=RequestMethod.POST)
    public ModelAndView postUserProfile(
        @PathVariable String userName,
        Authentication authentication
    ) {
        ModelAndView modelAndView = new ModelAndView("redirect:/logout");
        userService.deleteUser(userName);
        return modelAndView;
    }
}

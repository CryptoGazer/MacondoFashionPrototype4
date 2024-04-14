package com.macondo_cs.MacondoFashionPrototype4.config;

import java.security.Principal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalControllerAdvice {
    @ModelAttribute
    public void addAttributes(Model model, Principal principal) {
        log.info("\nPrincipal auth: {}\n", isAuthorized(principal));
        model.addAttribute("username", principal != null ? principal.getName() : "");
        model.addAttribute("authorized", isAuthorized(principal));
    }

    private boolean isAuthorized(Principal principal) {
        log.info("\nPrincipal: {}\n", principal);
        if (principal != null) {
            // Collection<? extends GrantedAuthority> authorities = principal.getAuthorities();
            return true; 
        }
        return false;
    }
}
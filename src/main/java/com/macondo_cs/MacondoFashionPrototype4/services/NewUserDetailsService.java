package com.macondo_cs.MacondoFashionPrototype4.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.macondo_cs.MacondoFashionPrototype4.config.NewUserDetails;
import com.macondo_cs.MacondoFashionPrototype4.models.User;
import com.macondo_cs.MacondoFashionPrototype4.repo.UserRepository;

@Service
public class NewUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository repository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repository.findByName(username);
        return user.map(NewUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("\n\t" + username + " not found!\n"));
    }
}

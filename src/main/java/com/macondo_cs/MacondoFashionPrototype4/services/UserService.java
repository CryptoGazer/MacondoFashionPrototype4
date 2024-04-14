package com.macondo_cs.MacondoFashionPrototype4.services;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.macondo_cs.MacondoFashionPrototype4.models.*;
import com.macondo_cs.MacondoFashionPrototype4.repo.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    // passwords hashing
    @Autowired
    private PasswordEncoder passwordEncoder;

    // public Application applicationById(Long id) {
    //     return applications.stream()
    //         .filter(app -> app.getId().equals(id))
    //         .findFirst()
    //         .orElse(null);
    // }

    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setDatetimeRegirested();
        user.setTotalBought(0);
        user.setTotalSpent(0);
        // user.setEmail("");
        // user.setRoles("ROLE_USER, ROLE_ADMIN");
        userRepository.save(user);
    }

    public void registerUser(User user) {
        log.info("\nCurrent User: \n {} | {}",
        user.getName(), user.getEmail()
        );

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setDatetimeRegirested();
        user.setTotalBought(0);
        user.setTotalSpent(0);
        user.setRoles("ROLE_USER");
        userRepository.save(user);
    }

    public List<NewUserDTO> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map((user) -> mapToNewUserDTO(user))
                .collect(Collectors.toList());
    }

    public User findByName(String name) {
        Optional<User> optionalUser = userRepository.findByName(name);
        if (optionalUser != null) {
            return optionalUser.get();
        }
        return null;
    }

    @Transactional
    public void deleteUser(String userName) {
        userRepository.deleteByName(userName);
    }

    private NewUserDTO mapToNewUserDTO(User user) {
        NewUserDTO userDTO = new NewUserDTO();
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setDatetimeRegirested(user.getDatetimeRegirested());
        userDTO.setTotalBought(user.getTotalBought());
        userDTO.setTotalSpent(user.getTotalSpent());
        userDTO.setRoles(user.getRoles());
        return userDTO;
    }


}

package com.macondo_cs.MacondoFashionPrototype4.repo;

import com.macondo_cs.MacondoFashionPrototype4.models.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
    void deleteByName(String Name);
}

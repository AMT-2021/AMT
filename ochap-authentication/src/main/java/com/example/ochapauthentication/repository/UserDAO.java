package com.example.ochapauthentication.repository;

import com.example.ochapauthentication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDAO extends JpaRepository<User, Integer> {
    Optional<User> getUserByUsername(String username);
}

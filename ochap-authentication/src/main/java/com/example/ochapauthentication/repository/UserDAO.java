package com.example.ochapauthentication.repository;

import com.example.ochapauthentication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User, Integer> {
    User getUserByUsername(String username);
}

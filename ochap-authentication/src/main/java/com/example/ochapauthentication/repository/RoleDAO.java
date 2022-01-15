package com.example.ochapauthentication.repository;

import com.example.ochapauthentication.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDAO extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}

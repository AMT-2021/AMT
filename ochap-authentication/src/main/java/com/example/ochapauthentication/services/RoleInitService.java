package com.example.ochapauthentication.services;

import com.example.ochapauthentication.entities.Role;
import com.example.ochapauthentication.repository.RoleDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleInitService {

    RoleDAO roleRepository;


    public RoleInitService(RoleDAO roleRepository){
        this.roleRepository = roleRepository;
        if(roleRepository.findByName("user") == null){
            Role user = new Role();
            user.setName("user");
            roleRepository.save(user);
        }

        if(roleRepository.findByName("admin") == null){
            Role admin = new Role();
            admin.setName("admin");
            roleRepository.save(admin);
        }
    }
}

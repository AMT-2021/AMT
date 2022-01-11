package com.example.ochapauthentication.routes;

import com.example.ochapauthentication.commands.AuthLoginCommand;
import com.example.ochapauthentication.entities.User;
import com.example.ochapauthentication.repository.RoleDAO;
import com.example.ochapauthentication.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
public class AuthenticationController {

    final UserDAO userRepository;
    final RoleDAO roleRepository;

    @Autowired
    public AuthenticationController(UserDAO userRepository, RoleDAO roleRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;

    }

    @PostMapping("/auth/login")
    @ResponseBody
    String loginAutentication(@RequestBody AuthLoginCommand credentials){
        return "";
    }

    @PostMapping("/accounts/register")
    @ResponseBody
    String registerAutentication(@RequestBody AccountRegisterCommand credentials){

        return "";
    }
}

package com.example.ochapauthentication.routes;

import com.example.ochapauthentication.commands.AccountRegisterCommand;
import com.example.ochapauthentication.commands.AuthLoginCommand;
import com.example.ochapauthentication.dto.AccountInfoDTO;
import com.example.ochapauthentication.entities.User;
import com.example.ochapauthentication.repository.RoleDAO;
import com.example.ochapauthentication.repository.UserDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Optional;

@Controller
public class AuthenticationController {

    final UserDAO userRepository;
    final RoleDAO roleRepository;
    final PasswordEncoder passwordEncoder;
    final DaoAuthenticationProvider authProvider;
    @Autowired
    AuthenticationController(UserDAO userRepository, RoleDAO roleRepository, PasswordEncoder passwordEncoder,
    DaoAuthenticationProvider authProvider){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authProvider = authProvider;
    }

    @PostMapping(value = "/auth/login",  produces = MediaType.APPLICATION_JSON_VALUE    )
    @ResponseBody
    String loginAutentication(@RequestBody AuthLoginCommand credentials){
        Optional<User> user = userRepository.getUserByUsername(credentials.getUsername());
        //TODO
        return "";
    }

    @PostMapping(value = "/accounts/register",  produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String registerAutentication(@RequestBody AccountRegisterCommand credentials) throws JsonProcessingException {
        if (userRepository.getUserByUsername(credentials.getUsername()).isPresent()){
            return "{ \"error\" :  \"user already exists\"}";
        }

        // TODO Verify password condition

        User user = new User();
        user.setUsername(credentials.getUsername());
        user.setPassword(passwordEncoder.encode(credentials.getPassword()));
        user.setRole(roleRepository.getRoleByName("user"));
        User userCreated = userRepository.save(user);

        ObjectMapper objectMapper = new ObjectMapper();

        AccountInfoDTO accountInfo = new AccountInfoDTO();
        accountInfo.setId(userCreated.getId());
        accountInfo.setUsername(userCreated.getUsername());
        accountInfo.setRole(userCreated.getRole().getName());

        return objectMapper.writeValueAsString(accountInfo);
    }
}

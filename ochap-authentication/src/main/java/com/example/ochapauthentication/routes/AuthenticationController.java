package com.example.ochapauthentication.routes;

import com.example.ochapauthentication.commands.AccountRegisterCommand;
import com.example.ochapauthentication.commands.AuthLoginCommand;
import com.example.ochapauthentication.dto.AccountInfoDTO;
import com.example.ochapauthentication.entities.Role;
import com.example.ochapauthentication.dto.TokenDTO;
import com.example.ochapauthentication.entities.User;
import com.example.ochapauthentication.jwt.JwtTokenUtil;
import com.example.ochapauthentication.repository.RoleDAO;
import com.example.ochapauthentication.repository.UserDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Optional;

@Controller
public class AuthenticationController {

    final UserDAO userRepository;
    final RoleDAO roleRepository;

    @Autowired
    AuthenticationController(UserDAO userRepository, RoleDAO roleRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Autowired
    private JwtTokenUtil jwt;

    @PostMapping("/auth/login")
    @ResponseBody
    String loginAutentication(@RequestBody AuthLoginCommand credentials) throws JsonProcessingException {
        Optional<User> user = userRepository.getUserByUsername(credentials.getUsername());
        if (user.isEmpty()){
            return "{\"error\" : \"user don't exist\"}";
        }

        byte[] hashedPassword = hashPassword(credentials.getPassword(), user.get().getSalt());
        if (!Arrays.equals(hashedPassword, user.get().getPassword())) {
            return "{\"error\" : \"wrong password\"}";
        }

        // Generate JWT
        User u = user.get();
        AccountInfoDTO accountInfo = new AccountInfoDTO();
        accountInfo.setUsername(u.getUsername());
        accountInfo.setId(u.getId());
        accountInfo.setRole(u.getRole().getName());

        TokenDTO tokenDTO = new TokenDTO();
        String token = jwt.generateToken(accountInfo);
        System.out.println(token);
        tokenDTO.setToken(token);
        tokenDTO.setAccountInfo(accountInfo);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(tokenDTO);
    }

    @PostMapping(value = "/accounts/register",  produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    String registerAutentication(@RequestBody AccountRegisterCommand credentials) throws JsonProcessingException {


        if (userRepository.getUserByUsername(credentials.getUsername()).isPresent()) {
          //TODO
        }

        User user = new User();
        user.setUsername(credentials.getUsername());

        byte[] salt = generateSalt();
        byte[] hashedPasssword = hashPassword(credentials.getPassword(), salt);
        user.setPassword(hashedPasssword);
        user.setSalt(salt);
        Role role = roleRepository.findByName("user");
        user.setRole(role);
        User userCreated = userRepository.save(user);


        ObjectMapper objectMapper = new ObjectMapper();

        AccountInfoDTO accountInfo = new AccountInfoDTO();
        accountInfo.setId(userCreated.getId());
        accountInfo.setUsername(userCreated.getUsername());
        accountInfo.setRole(userCreated.getRole().getName());

        return objectMapper.writeValueAsString(accountInfo);
    }

    private byte[] hashPassword(String password, byte[] salt){

        try{
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            return md.digest(password.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private byte[] generateSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
}

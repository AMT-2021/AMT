package com.example.ochapauthentication.routes;

import com.example.ochapauthentication.commands.AuthLoginCommand;
import com.example.ochapauthentication.repository.RoleDAO;
import com.example.ochapauthentication.repository.UserDAO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthenticationController {

    UserDAO userRepository; //TODO: Not use Autowired to build
    RoleDAO roleRepository; //TODO: Not use Autowired to build
    @PostMapping("/auth/login")
    @ResponseBody
    String loginAutentication(@RequestBody AuthLoginCommand credentials){
        return "";
    }

    @PostMapping("/accounts/register")
    @ResponseBody
    String registerAutentication(@RequestBody AuthLoginCommand credentials){

        return "";
    }
}

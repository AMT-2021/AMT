package com.example.ochapauthentication.commands;

import lombok.Data;

@Data
public class AccountRegisterCommand {
    private String username;
    private String password;
}

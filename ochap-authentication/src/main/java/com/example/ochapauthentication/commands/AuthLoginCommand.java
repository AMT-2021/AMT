package com.example.ochapauthentication.commands;

import lombok.Data;

@Data
public class AuthLoginCommand {
  private String username;
  private String password;
}

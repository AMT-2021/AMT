package com.example.ochapauthentication.dto;

import lombok.Data;

@Data
public class AccountInfoDTO {
  private Integer id;
  private String username;
  private String role;

  public AccountInfoDTO(Integer id, String username, String role) {
    this.id = id;
    this.username = username;
    this.role = role;
  }
}

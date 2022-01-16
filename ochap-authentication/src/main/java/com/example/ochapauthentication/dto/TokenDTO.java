package com.example.ochapauthentication.dto;

import lombok.Data;

@Data
public class TokenDTO {
  private String token;
  private AccountInfoDTO accountInfo;

  public TokenDTO(String token, AccountInfoDTO accountInfo) {
    this.token = token;
    this.accountInfo = accountInfo;
  }
}

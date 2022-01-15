package com.example.ochapauthentication.dto;

import lombok.Data;

@Data
public class TokenDTO {
    private String token;
    private AccountInfoDTO account;

    public TokenDTO(String token, AccountInfoDTO account) {
        this.token = token;
        this.account = account;
    }
}

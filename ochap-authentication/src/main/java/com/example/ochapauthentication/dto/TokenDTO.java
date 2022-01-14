package com.example.ochapauthentication.dto;

import lombok.Data;

@Data
public class TokenDTO {
    private String token;
    private AccountInfoDTO accountInfo;
}

package com.example.ochapauthentication.dto;

import lombok.Data;
import org.springframework.boot.jackson.JsonComponent;

@Data
public class AccountInfoDTO {
    private Integer id;
    private String username;
    private String role;
}

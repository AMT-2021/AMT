package com.example.ochapauthentication.dto;

import lombok.Data;

@Data
public class ErrorDTO {
  private String property;
  private String message;

  public ErrorDTO(String property, String message) {
    this.property = property;
    this.message = message;
  }
}

package com.example.ochapauthentication.dto;

import lombok.Data;

@Data
public class Error {
  private String error;

  public Error(String error) {
    this.error = error;
  }
}

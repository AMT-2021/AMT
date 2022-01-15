package com.example.ochapauthentication.dto;

import lombok.Data;

import java.util.List;

@Data
public class ErrorsDTO {
    private List<ErrorDTO> errors;

    public ErrorsDTO(List<ErrorDTO> errors) {
        this.errors = errors;
    }
}

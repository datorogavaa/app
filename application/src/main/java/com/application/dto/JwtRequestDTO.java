package com.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class JwtRequestDTO {
    @NotNull(message = "Owner phone number is required")
    @Pattern(
            regexp = "^(79\\d{7}|5\\d{8})$",
            message = "Phone number must be a valid Georgian number without +995"
    )
    private Integer number;


    public JwtRequestDTO(Integer number) {
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}

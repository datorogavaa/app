package com.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class OtpVerifyRequestDTO{

    @NotNull(message = "Owner phone number is required")
    @Pattern(
            regexp = "^(79\\d{7}|5\\d{8})$",
            message = "Phone number must be a valid Georgian number without +995"
    )


    @NotNull(message = "Code is required")
    @Pattern(
            regexp = "^[0-9]{6}$",
            message = "Code must be a 6-digit number"
    )
    private Integer number;

    private String code;

    public OtpVerifyRequestDTO(Integer number, String code) {
        this.number = number;
        this.code = code;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

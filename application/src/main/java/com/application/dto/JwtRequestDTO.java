package com.application.dto;

public class JwtRequestDTO {
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

package com.application.dto;

public class OtpVerifyRequestDTO{
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

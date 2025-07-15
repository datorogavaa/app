package com.application.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDateTime;
import java.util.List;

public class HomeDTO {

    @Null(message = "ID must not be provided manually") // Usually auto-generated
    private Long id;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address must be less than 255 characters")
    private String address;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    @NotNull(message = "Door code is required")
    @Min(value = 1000, message = "Door code must be a 4-digit number")
    @Max(value = 9999, message = "Door code must be a 4-digit number")
    private Integer doorCode;

    @NotNull(message = "Number of bedrooms is required")
    @Min(value = 1, message = "Must have at least 1 bedroom")
    private Integer bedrooms;

    @NotBlank(message = "Owner name is required")
    @Size(max = 100, message = "Owner name must be less than 100 characters")
    private String ownerName;

    @NotNull(message = "Owner phone number is required")
    @Pattern(
            regexp = "^(79\\d{7}|5\\d{8})$",
            message = "Phone number must be a valid Georgian number without +995"
    )
    private String ownerNumber;


    private LocalDateTime rentedDate;

    private LocalDateTime rentedUntil;

    private List<String> signedImageUrls;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDoorCode() {
        return doorCode;
    }

    public void setDoorCode(Integer doorCode) {
        this.doorCode = doorCode;
    }

    public Integer getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(Integer bedrooms) {
        this.bedrooms = bedrooms;
    }

    public String getOwnerNumber() {
        return ownerNumber;
    }

    public void setOwnerNumber(String ownerNumber) {
        this.ownerNumber = ownerNumber;
    }

    public LocalDateTime getRentedDate() {
        return rentedDate;
    }

    public void setRentedDate(LocalDateTime rentedDate) {
        this.rentedDate = rentedDate;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public LocalDateTime getRentedUntil() {
        return rentedUntil;
    }

    public void setRentedUntil(LocalDateTime rentedUntil) {
        this.rentedUntil = rentedUntil;
    }

    public List<String> getSignedImageUrls() {
        return signedImageUrls;
    }

    public void setSignedImageUrls(List<String> signedImageUrls) {
        this.signedImageUrls = signedImageUrls;
    }
}

package com.application.model;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.time.LocalDateTime;


@Entity
@Table(name = "homes")
public class Home {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /// მისამართი
    @Column(name = "address")
    private String address;
    /// სახლის ფასი ერთი დღე
    @Column(name = "price")
    private Double price;

    /// სახლის აღწერა
    @Column(name = "description")
    private String description;

    /// სახლის კოდი
    @Column(name = "doorCode")
    private Integer doorCode;

    ///  სახლის მფლობელის სახელი
    @Column(name = "ownerName")
    private String ownerName;

    ///  სახლის მფლობელის ტელეფონის ნომერი
    @Column(name = "ownerNumber")
    private String ownerNumber;

    /// საძინებლების რაოდენობა
    @Column(name = "bedrooms")
    private Integer bedrooms;

    @ElementCollection
    @CollectionTable(
            name = "home_image_urls",
            joinColumns = @JoinColumn(name = "home_id")
    )
    @Column(name = "image_url")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<String> imageUrls;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "user_id", nullable = true)
    private User user;


    private LocalDateTime rentedDate;

    private LocalDateTime rentedUntil;


    public Home(Long id, String address, Double price, String description, Integer doorCode, String ownerName, String ownerNumber, Integer bedrooms ,List<String> imageUrls) {
        this.id = id;
        this.address = address;
        this.price = price;
        this.description = description;
        this.doorCode = doorCode;
        this.ownerName = ownerName;
        this.ownerNumber = ownerNumber;
        this.bedrooms = bedrooms;
        this.imageUrls = imageUrls;
    }
    public Home() {
    }






    @Transient
    private List<String> signedImageUrls;

    public List<String> getSignedImageUrls() {
        return signedImageUrls;
    }

    public void setSignedImageUrls(List<String> signedImageUrls) {
        this.signedImageUrls = signedImageUrls;
    }





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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerNumber() {
        return ownerNumber;
    }

    public void setOwnerNumber(String ownerNumber) {
        this.ownerNumber = ownerNumber;
    }

    public Integer getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(Integer bedrooms) {
        this.bedrooms = bedrooms;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getRentedDate() {
        return rentedDate;
    }

    public void setRentedDate(LocalDateTime rentedDate) {
        this.rentedDate = rentedDate;
    }

    public LocalDateTime getRentedUntil() {
        return rentedUntil;
    }

    public void setRentedUntil(LocalDateTime rentedUntil) {
        this.rentedUntil = rentedUntil;
    }
}
package com.application.model;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.time.LocalDateTime;


@Entity
@Table(name = "homes")
public class Home {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    /// განცხადების სახელი
    @Column(name = "post_name")
    private String postName;
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
    @Column(name = "code")
    private int code;

    @ElementCollection
    @CollectionTable(
            name = "home_image_urls",
            joinColumns = @JoinColumn(name = "home_id")
    )
    @Column(name = "image_url")
    private List<String> imageUrls;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "user_id", nullable = true)
    private User user;


    @Column
    private LocalDateTime rentedDate;

    @Column
    private LocalDateTime rentedUntil;

    public Home(String postName, String address, Double price, String description,Integer code) {
        this.postName = postName;
        this.address = address;
        this.price = price;
        this.description = description;
        this.code = code;
    }
    public Home() {
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
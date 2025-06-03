package com.application.model;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


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

    @Column(name = "image_urls", length = 4096)
    private String imageUrls;


    @Column(name = "user_id", nullable = true)
    private UUID userId;  // Use UUID for Keycloak user ID

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }


    public Home() {

    }

    public Home(String postName, String address, Double price, String description,Integer code) {
        this.postName = postName;
        this.address = address;
        this.price = price;
        this.description = description;
        this.code = code;
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


    public List<String> getImageUrlList() {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(imageUrls.split(","));
    }

    public void setImageUrlList(List<String> urls) {
        this.imageUrls = String.join(",", urls);
    }


}
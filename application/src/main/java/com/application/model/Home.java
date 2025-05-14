package com.application.model;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;


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

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id",  nullable = true)
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}
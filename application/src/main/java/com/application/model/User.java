package com.application.model;


import com.application.smsverification.SmsVerification;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


//    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
//    @JsonManagedReference
//    private Set<UserRole> roles = new HashSet<>();
//
//    public Set<UserRole> getRoles() {
//        return roles;
//    }

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Home> homes = new HashSet<>();

    public Set<Home> getHomes() {
        return homes;
    }

    public void setHomes(Set<Home> homes) {
        this.homes = homes;
    }

    @Column(name = "number")
    private  String number;

    @Column(name = "password")
    private String password;

    public long getId() {
        return id;
    }

    public User() {

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String number, String password) {
        this.number = number;
        this.password = password;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}

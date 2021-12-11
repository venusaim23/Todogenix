package com.alanai.todogenix.Models;

public class UserDetails {
    private String uid;
    private String name;
    private String email;
    private String city;

    public UserDetails() {
    }

    public UserDetails(String name, String email, String city) {
        this.name = name;
        this.email = email;
        this.city = city;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}

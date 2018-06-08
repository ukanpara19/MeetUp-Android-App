package com.example.darshilbhayani.meetup_group1mobileapp;

import java.util.HashMap;

public class User {
    public String email;
    public String name;
    public String password;
    public String number;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public User(){
    }

    public User(String name, String email, String number){
        this.name = name;
        this.email = email;
        this.number = number;
    }
}

package com.example.darshilbhayani.meetup_group1mobileapp;

import java.util.HashMap;

public class User {
    public String email;
    public String name;
    public String password;
    public String number;

    public User(){

    }

    public User(String name, String email, String number){
        this.name = name;
        this.email = email;
        this.number = number;
    }
}

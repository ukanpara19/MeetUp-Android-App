package com.example.darshilbhayani.meetup_group1mobileapp;

import java.util.HashMap;

public class User {
    public String email;
    public String name;
    public String password;
    public String number;
    HashMap<String,String> contact;

    public User(){

    }

    public User(String name, String email, String number, HashMap<String,String> contact){
        this.name = name;
        this.email = email;
        this.number = number;
        this.contact = contact;
    }
}

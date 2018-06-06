package com.example.darshilbhayani.meetup_group1mobileapp;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;

public class CreatePage4 extends AppCompatActivity {
    HashMap<String, String> event = new HashMap<>();
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static String[] PERMISSIONS_CONTACT = {Manifest.permission.READ_CONTACTS};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_plan4);
    }
}

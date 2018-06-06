package com.example.darshilbhayani.meetup_group1mobileapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CreatePage4 extends AppCompatActivity {
    HashMap<String, String> event = new HashMap<>();
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static String[] PERMISSIONS_CONTACT = {Manifest.permission.READ_CONTACTS};
    private HashMap<String, String> contactHash;
    private DatabaseReference mDatabase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_plan4);
        Intent intent = getIntent();
        event = (HashMap<String, String>)intent.getSerializableExtra("hashmap");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        event.put("email_id","dbp3435@gmail.com");
        mDatabase.child("event").child("2").setValue(event);
        mDatabase.child("event").child("3").setValue(event);
        mDatabase.child("event").child("4").setValue(event);
        mDatabase.child("event").child("5").setValue(event);

        Log.d("event",event+"");
        permissionEnable();
    }

    public void permissionEnable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(PERMISSIONS_CONTACT, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            getContactList();
        }
    }
    private void getContactList() {
        String phoneNumber = "";
        HashMap<String, String> contact = new HashMap<String, String>();
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {

                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if (Integer.parseInt(hasPhone) > 0) {
                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,  null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
                    if(phones.getCount()>0){
                        while (phones.moveToNext()) { //iterate over all contact phone numbers
                            phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }
                    }
                    phones.close();
                }
                Log.d(name,phoneNumber);
                contact.put(name,phoneNumber);
            } while (cursor.moveToNext());
        }
        cursor.close();
        setContact(contact);
    }
    private void setContact(HashMap<String, String> contact) {
        this.contactHash = contact;
        Log.d("contact",contact+"");
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d(requestCode+" hey yea",PERMISSIONS_REQUEST_READ_CONTACTS+"");
        switch (requestCode) {
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! do the
                    // calendar task you need to do.
                    Log.d("dhaval", "permission granted");
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d("dhaval", "permission denied");
                }
                return;
            }
        }
        Log.d("dhaval","error");
    }

}

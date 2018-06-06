package com.example.darshilbhayani.meetup_group1mobileapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class requestContactPermission extends AppCompatActivity {

    private static final int REQUEST_READ_CONTACTS = 79 ;
    private ArrayList<String> nameArrayList;
    private HashMap<String, String> contactHash;
    private HashMap<String, String> previousIntentHashMap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent1 = getIntent();
        previousIntentHashMap = (HashMap<String, String>) intent1.getSerializableExtra("hashmap");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            getContactList();
        } else {
            requestContactPerm();
        }
    }

    private void requestContactPerm() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.READ_CONTACTS)) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACTS: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContactList();

                } else {
                }
                return;
            }

        }
    }

    private void getContactList() {
        String phoneNumber = "";
        HashMap<String, String> contact = new HashMap<String, String>();
        nameArrayList = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {

                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                nameArrayList.add(name);
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if (Integer.parseInt(hasPhone) > 0) {
                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,  null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
                    if(phones.getCount()>0){
                        while (phones.moveToNext()) {
                            phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }
                    }
                    phones.close();
                }
                contact.put(name,phoneNumber);
            } while (cursor.moveToNext());
        }
        cursor.close();
        setContact(contact);
    }
    private void setContact(HashMap<String, String> contact) {
        this.contactHash = contact;
        Intent intent = new Intent(requestContactPermission.this,CreatePage4.class);
        intent.putExtra("contact",contactHash);
        intent.putExtra("hashmap",previousIntentHashMap);
        intent.putExtra("nameArrayList",nameArrayList);
        startActivity(intent);
    }
}

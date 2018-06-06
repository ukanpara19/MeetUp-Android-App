package com.example.darshilbhayani.meetup_group1mobileapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.HashMap;

public class CreatePage4 extends AppCompatActivity {
    HashMap<String, String> event = new HashMap<>();
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static String[] PERMISSIONS_CONTACT = {Manifest.permission.READ_CONTACTS};
    private HashMap<String, String> contactHash;
    private DatabaseReference mDatabase;
    Button reviewPlanButton;
    ArrayList<String> nameArrayList;
    HashMap<String,String> selectedNumbers = new HashMap<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_plan4);
        final Intent intent = getIntent();
        event = (HashMap<String, String>)intent.getSerializableExtra("hashmap");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("event").child("1").setValue(event);

        permissionEnable();
        final HashMap<String, String> contactHash1 = getContact();


        final ListView myList = (ListView) findViewById(R.id.newList);
        reviewPlanButton = (Button) findViewById(R.id.button4);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1,nameArrayList);
        myList.setAdapter(adapter);

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(selectedNumbers.containsKey(nameArrayList.get(position))){
                    selectedNumbers.remove(nameArrayList.get(position));
                    view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }else {
                    selectedNumbers.put(nameArrayList.get(position), contactHash1.get(nameArrayList.get(position)));
                    view.setBackgroundColor(Color.parseColor("#8FD8D8"));
                }
            }
        });

        reviewPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.put("Invited People",selectedNumbers+"");
                Intent intent1 = new Intent(CreatePage4.this,CreatePage5.class);
                intent1.putExtra("hashmap",event);
                startActivity(intent1);
            }
        });
    }

    public void permissionEnable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(PERMISSIONS_CONTACT, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            getContactList();
        }
    }
    public HashMap<String,String> getContact(){
        return contactHash;
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
        Log.d("contact",contact+"");
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d(requestCode+" hey yea",PERMISSIONS_REQUEST_READ_CONTACTS+"");
        switch (requestCode) {
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(CreatePage4.this,"Permission Denied",Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}

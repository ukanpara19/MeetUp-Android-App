package com.example.darshilbhayani.meetup_group1mobileapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
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
    private static final int PERMISSION_REQUEST_CONTACT = 1;
    HashMap<String, String> event = new HashMap<>();
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static String[] PERMISSIONS_CONTACT = {Manifest.permission.READ_CONTACTS};
    private HashMap<String, String> previousIntentHashMap;
    private DatabaseReference mDatabase;
    Button reviewPlanButton;
    ArrayList<String> nameArrayList = null;
    public static final String MainPP_SP = "MainPP_data";
    public static final int R_PERM = 2822;
    private static final int REQUEST= 112;
    HashMap<String,String> selectedNumbers = new HashMap<>();
    ListView myList;
    Context mContext = this;
    ArrayAdapter<String> adapter = null;
    HashMap<String, String> contactHash1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_plan4);
        final Intent intent = getIntent();
        event = (HashMap<String, String>)intent.getSerializableExtra("hashmap");
        nameArrayList = intent.getStringArrayListExtra("nameArrayList");
        contactHash1 = (HashMap<String, String>) intent.getSerializableExtra("contact");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        reviewPlanButton = (Button) findViewById(R.id.button4);

        myList = (ListView) findViewById(R.id.newList);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1,nameArrayList);
        myList.setAdapter(adapter);
        myList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d("ID", id +"");

                Log.d("Sel ID", parent.getSelectedItemId()+"");
                if(selectedNumbers.containsKey(nameArrayList.get(position))){
                    selectedNumbers.remove(nameArrayList.get(position));
                    //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }else {
                    Log.d("error",nameArrayList.get(position)+"");
                    Log.d("error",contactHash1+"");
                    selectedNumbers.put(nameArrayList.get(position), contactHash1.get(nameArrayList.get(position)));

                    //view.setBackgroundColor(Color.parseColor("#8FD8D8"));
                }
                Log.d("data",selectedNumbers+"");
            }
        });

        reviewPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(CreatePage4.this,CreatePage5.class);
                intent1.putExtra("hashmap",event);
                intent1.putExtra("hashmapofinvitedpeople",selectedNumbers);
                startActivity(intent1);
            }
        });
    }
}

package com.example.darshilbhayani.meetup_group1mobileapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Semaphore;

public class UserProfile extends AppCompatActivity {

    private FirebaseDatabase mFireBaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private DatabaseReference mDatabase;

    static HashMap<String,User> userDetails = new HashMap<>();

    TextView userNm,userEmailId,userPhno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);


        userNm = (TextView) findViewById(R.id.tv_name);
        userEmailId = (TextView) findViewById(R.id.userEmailId);
        userPhno = (TextView) findViewById(R.id.userPhno);

            SharedPreferences editor = getApplicationContext().getSharedPreferences(LoginDemo.MY_PREFS_NAME, MODE_PRIVATE);
            String loogedInUser = editor.getString("Email_ID","darshilbhayani92@gmail.com");

            if(userDetails.keySet().contains(loogedInUser.toString())){
                User currUserData = userDetails.get(loogedInUser.toString());

                userNm.setText(currUserData.getName()==null?"":currUserData.getName());
                userEmailId.setText(currUserData.getEmail()==null?"":currUserData.getEmail());
                userPhno.setText(currUserData.getNumber()==null?"":currUserData.getNumber());
            }

        getUserData();
    }

    HashMap<String,Event> localEvent = new HashMap<>();

    private void getUserData() {

        try {
            Intent intent = getIntent();

            mAuth = FirebaseAuth.getInstance();
            mFireBaseDatabase = FirebaseDatabase.getInstance();
            mDatabase = mFireBaseDatabase.getReference();

            FirebaseUser user = mAuth.getCurrentUser();
            mAuthListner = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if(user!=null){
                        Log.d("User!",user.getUid());
                    }else{
                        Log.d("User","User is null!");
                    }
                }
            };

            // final Semaphore semaphore = new Semaphore(0);
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.i("Total Record...!!",dataSnapshot.getChildrenCount()+"");

                    for(DataSnapshot iDDataSet : dataSnapshot.getChildren())
                    {
                        if(iDDataSet.getKey().equals("event")){

                            for(DataSnapshot eventDataByEventKey : iDDataSet.getChildren()) {

                                Log.i("Total Record...!!",eventDataByEventKey.getChildrenCount()+"");
                                for(DataSnapshot eventIDDataSet : eventDataByEventKey.getChildren())
                                {
                                    Log.i(eventIDDataSet.getKey(),eventIDDataSet.getChildrenCount() + "");
                                    Event e1 = new Event();
                                    for(DataSnapshot eventData : eventIDDataSet.getChildren())
                                    {
                                        /*if(eventDatalocal.getKey().equals("ppl_joined"))
                                             Log.i(eventDatalocal.getKey()+"**","--"+eventDatalocal.getValue().toString());*/

                                        if(eventData.getKey().equals("email_id"))
                                            e1.setEmail_id(eventData.getValue().toString());
                                        if(eventData.getKey().equals("event_date"))
                                            e1.setEvent_date(eventData.getValue().toString());
                                        if(eventData.getKey().equals("event_dest"))
                                            e1.setEvent_dest(eventData.getValue().toString());
                                        if(eventData.getKey().equals("event_duration"))
                                            e1.setEvent_duration(eventData.getValue().toString());
                                        if(eventData.getKey().equals("event_name"))
                                            e1.setEvent_name(eventData.getValue().toString());
                                        if(eventData.getKey().equals("event_source"))
                                            e1.setEvent_source(eventData.getValue().toString());
                                        if(eventData.getKey().equals("event_time"))
                                            e1.setEvent_time(eventData.getValue().toString());
                                        if(eventData.getKey().equals("event_type"))
                                            e1.setEvent_type(eventData.getValue().toString());
                                        if(eventData.getKey().equals("lan_dest"))
                                            e1.setLan_dest(eventData.getValue().toString());
                                        if(eventData.getKey().equals("lan_source"))
                                            e1.setLan_source(eventData.getValue().toString());
                                        if(eventData.getKey().equals("lat_dest"))
                                            e1.setLat_dest(eventData.getValue().toString());
                                        if(eventData.getKey().equals("lat_source"))
                                            e1.setLat_source(eventData.getValue().toString());
                                        if(eventData.getKey().equals("ppl_joined"))
                                            e1.setppl_joined(eventData.getValue().toString());
                                    }
                                    localEvent.put(eventDataByEventKey.getKey(),e1);
                                }
                            }
                        }

                        if(iDDataSet.getKey().equals("users")){
                            for(DataSnapshot userDataByUserKey : iDDataSet.getChildren()) {
                                User u1 = new User();
                                u1.setEmail(userDataByUserKey.getKey());
                                for (DataSnapshot userData : userDataByUserKey.getChildren()) {
                                    if (userData.getKey().equals("email"))
                                        u1.setEmail(userData.getValue().toString());
                                    if (userData.getKey().equals("name"))
                                        u1.setName(userData.getValue().toString());
                                    if (userData.getKey().equals("number"))
                                        u1.setNumber(userData.getValue().toString());
                                }
                                userDetails.put(userDataByUserKey.getKey(), u1);
                            }

                        }else{
                            Log.i("Key!","is wrong in firebase");
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }catch (Exception e){

        }
    }
}


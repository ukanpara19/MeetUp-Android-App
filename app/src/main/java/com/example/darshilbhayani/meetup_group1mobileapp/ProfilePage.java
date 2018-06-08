package com.example.darshilbhayani.meetup_group1mobileapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProfilePage extends AppCompatActivity {

    private Button button_myplan;
    private  Button  button_joinedplan;

    private ProfilePage curr;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mtoggle;

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


        //-- Drawer button --
        mDrawerLayout = findViewById(R.id.drawerLayout_profilepage);
        mtoggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //-- Drawer Button --


        //----Navigation Drawer---

        Intent i = new Intent(this, MapsActivity.class);
        curr = this;

NavigationView navigationView = findViewById(R.id.nav_bar_profile);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

   @Override
 public boolean onNavigationItemSelected(MenuItem item) {

  int id = item.getItemId();
   Intent i;
       mDrawerLayout.closeDrawer(Gravity.LEFT);
 if (id == R.id.nav_Map) {
      i = new Intent(curr, MapsActivity.class);
        startActivity(i);

    } else if (id == R.id.nav_settings) {
       i=new Intent(curr,Settings_main.class);
         startActivity(i);
      }
     else if (id == R.id.nav_Addplan) {
     i = new Intent(curr, CreatePage1.class);
     startActivity(i);
 }
 else if (id == R.id.nav_myPlan) {
     i = new Intent(curr, MyPlanHistory.class);
     startActivity(i);
 }
 else if (id == R.id.nav_planHistory) {
     i = new Intent(curr, ListPlanDetails.class);
     startActivity(i);
 }

 else if (id == R.id.nav_profile) {
     i = new Intent(curr, ProfilePage.class);
     startActivity(i);
 }
 else if (id == R.id.nav_logout) {
     i = new Intent(curr, LoginDemo.class);
     SharedPreferences.Editor editor = getSharedPreferences(LoginDemo.MY_PREFS_NAME, MODE_PRIVATE).edit();
     editor.putString("logged_in", "No");
     editor.putString("Email_ID","");
     editor.apply();
     startActivity(i);
 }

    return true;
    }

    });

        userNm = findViewById(R.id.tv_name);
        userEmailId = findViewById(R.id.userEmailId);
        userPhno = findViewById(R.id.userPhno);

        getUserData();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mtoggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
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

                            SharedPreferences editor = getApplicationContext().getSharedPreferences(LoginDemo.MY_PREFS_NAME, MODE_PRIVATE);
                            String loogedInUser = editor.getString("Email_ID","darshilbhayani1892@gmail.com");

                            Log.i("loogedInUser","...!!!..."+loogedInUser);
                            String newLoogedInUser = loogedInUser.replace(".",",");

                            Log.i("loogedInUser","...!!!.AFTER.."+loogedInUser);

                            int i=1;
                            for(Map.Entry<String,User> userData : userDetails.entrySet()) {
                                User currUserData = userData.getValue();
                                Log.i("loogedInUser","...!!!.AFTER.."+currUserData.getEmail());
                                if(currUserData.getEmail().equals(loogedInUser) || currUserData.getEmail().equals(newLoogedInUser)){

                                    userNm.setText(currUserData.getName()==null?"":currUserData.getName());
                                    userEmailId.setText(currUserData.getEmail()==null?"":currUserData.getEmail());

                                    if(!currUserData.getNumber().equals(""))
                                        userPhno.setText(currUserData.getNumber()==null?"":currUserData.getNumber());
                                    else
                                        userPhno.setText("202-408-6445");
                                }
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
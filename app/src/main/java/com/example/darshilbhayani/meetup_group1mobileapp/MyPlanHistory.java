package com.example.darshilbhayani.meetup_group1mobileapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyPlanHistory extends AppCompatActivity {

    private MyPlanHistory plan;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mtoggle;


    private ListView rowDataView;
    private rowDataAdapterListPlans adapter;
    private List<rowDataListPlans> rowDataListPlans;

    private FirebaseDatabase mFireBaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private DatabaseReference mDatabase;

    HashMap<String,Event> event = new HashMap<>();
    HashMap<String, Integer> drawableImg = new HashMap<>();

    static MyPlanHistory curr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_myplan);

        //-- Drawer button --
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout_myPlans);
        mtoggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //-- Drawer Button --
        //----Navigation Drawer---

        Intent i = new Intent(this, MapsActivity.class);
        plan = this;

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_bar_myplan);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                int id = item.getItemId();
                Intent i;

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
                    startActivity(i);
                }

                return true;
            }

        });

        //----Navigation Drawer---

        curr = this;

        rowDataView = findViewById(R.id.newList);
        rowDataListPlans = new ArrayList<>();

        drawableImg.put("a",R.drawable.a);
        drawableImg.put("b",R.drawable.b);
        drawableImg.put("c",R.drawable.c);
        drawableImg.put("d",R.drawable.d);
        drawableImg.put("e",R.drawable.e);
        drawableImg.put("f",R.drawable.f);
        drawableImg.put("g",R.drawable.g);
        drawableImg.put("h",R.drawable.h);
        drawableImg.put("i",R.drawable.i);
        drawableImg.put("j",R.drawable.j);
        drawableImg.put("k",R.drawable.k);
        drawableImg.put("l",R.drawable.l);
        drawableImg.put("m",R.drawable.m);
        drawableImg.put("n",R.drawable.n);
        drawableImg.put("o",R.drawable.o);
        drawableImg.put("p",R.drawable.p);
        drawableImg.put("q",R.drawable.q);
        drawableImg.put("r",R.drawable.r);
        drawableImg.put("s",R.drawable.s);
        drawableImg.put("t",R.drawable.t);
        drawableImg.put("u",R.drawable.u);
        drawableImg.put("v",R.drawable.v);
        drawableImg.put("w",R.drawable.w);
        drawableImg.put("x",R.drawable.x);
        drawableImg.put("y",R.drawable.y);
        drawableImg.put("z",R.drawable.z);

        rowDataView = findViewById(R.id.listViewProduct);
        rowDataListPlans = new ArrayList<>();

        fetchData();

        rowDataView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), MapNavigation.class);
                Log.i("Tag!", String.valueOf(view.getTag()));
                Event e1Tmp = event.get(String.valueOf(view.getTag()));
                i.putExtra("EVENT",e1Tmp);
                i.putExtra("ID",String.valueOf(view.getTag()));
                startActivity(i);
            }
        });
    }

    private void fetchData() {

        Intent intent = getIntent();

        mAuth = FirebaseAuth.getInstance();
        mFireBaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = mFireBaseDatabase.getReference();

        FirebaseUser user = mAuth.getCurrentUser();
        //uId = user.getUid();

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

        mDatabase.child("event").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("Total Record...!!",dataSnapshot.getChildrenCount()+"");
                for(DataSnapshot eventIDDataSet : dataSnapshot.getChildren())
                {
                    // Log.i(eventIDDataSet.getKey(),eventIDDataSet.getChildrenCount() + "");
                    Event e1 = new Event();
                    for(DataSnapshot eventDatalocal : eventIDDataSet.getChildren())
                    {
                        /*if(eventDatalocal.getKey().equals("ppl_joined"))
                             Log.i(eventDatalocal.getKey()+"**","--"+eventDatalocal.getValue().toString());*/

                        DataSnapshot eventData = eventDatalocal;

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
                    event.put(eventIDDataSet.getKey(),e1);
                }

                Log.i("event",event+"");

                SharedPreferences editor = getApplicationContext().getSharedPreferences(LoginDemo.MY_PREFS_NAME, MODE_PRIVATE);
                String loogedInUser = editor.getString("Email_ID","darshilbhayani1892@gmail.com");

                int i=1;
                for(Map.Entry<String,Event> evnData : event.entrySet()) {

                    Event e1 = evnData.getValue();
                    if(e1.getEmail_id().equals(loogedInUser) || e1.getppl_joined().contains(loogedInUser)) {
                        rowDataListPlans.add(new rowDataListPlans(Integer.parseInt(evnData.getKey()), e1.getEvent_name(),
                                drawableImg.get(String.valueOf(e1.getEvent_name().charAt(0)).toLowerCase()),
                                e1.getEvent_date(), e1.getEvent_time(), e1.getEvent_duration(), drawableImg.get(String.valueOf(e1.getEvent_name().charAt(0)).toLowerCase())));
                    }
                }
                adapter = new rowDataAdapterListPlans(getApplicationContext(), rowDataListPlans);
                rowDataView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //-- Drawer Button --
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mtoggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // -- Drawer Button --

}


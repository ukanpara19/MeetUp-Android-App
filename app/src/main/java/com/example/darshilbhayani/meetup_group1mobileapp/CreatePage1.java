package com.example.darshilbhayani.meetup_group1mobileapp;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.HashMap;
public class CreatePage1 extends AppCompatActivity {

    private CreatePage1 curr;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mtoggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_plan1);


        //-- Drawer button --
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout_createplan1);
        mtoggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //-- Drawer Button --

//----Navigation Drawer---

        Intent i = new Intent(this, MapsActivity.class);
        curr = this;

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_bar_createplan1);
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
                else if (id == R.id.nav_planHistory) {
                    i = new Intent(curr, ListPlanDetails.class);
                    startActivity(i);
                }

                else if (id == R.id.nav_profile) {
                    i = new Intent(curr, ProfilePage.class);
                    startActivity(i);
                }

                return true;
            }

        });

        //----Navigation Drawer---



        final HashMap<String,String> event_data = new HashMap<String, String>();
        CardView foodCardView = (CardView) findViewById(R.id.cardView1);
        CardView entertainmentCardView = (CardView) findViewById(R.id.cardView2);
        CardView sportsCardView = (CardView) findViewById(R.id.cardView3);
        CardView studyCardView = (CardView) findViewById(R.id.cardView4);
        CardView carpoolCardView = (CardView) findViewById(R.id.cardView5);
        CardView otherCardView = (CardView) findViewById(R.id.cardView6);
        final Event event = new Event();

        foodCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    event_data.put("event_type","Food");
                    event.setEvent_type("Food");
                    createPage2(event_data,event);
            }
        });

        entertainmentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event_data.put("event_type","Entertainment");
                event.setEvent_type("Entertainment");
                createPage2(event_data,event);
            }
        });

        sportsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event_data.put("event_type","Sports");
                event.setEvent_type("Sports");
                createPage2(event_data,event);
            }
        });

        studyCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event_data.put("event_type","Study");
                event.setEvent_type("Study");
                createPage2(event_data,event);
            }
        });

        carpoolCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event_data.put("event_type","Carpool");
                event.setEvent_type("Carpool");
                createPage2(event_data,event);
            }
        });

        otherCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event_data.put("event_type","Other");
                event.setEvent_type("Other");
                createPage2(event_data,event);
            }
        });
    }
    private void createPage2(HashMap<String, String> event_data, Event event) {
        Intent intent = new Intent(CreatePage1.this,CreatePage2.class);
        intent.putExtra("hashmap",event_data);
        intent.putExtra("event_object",event.getEvent_type());
        startActivity(intent);
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

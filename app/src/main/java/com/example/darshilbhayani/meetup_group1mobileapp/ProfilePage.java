package com.example.darshilbhayani.meetup_group1mobileapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

public class ProfilePage extends AppCompatActivity {

    private Button button_myplan;
    private  Button  button_joinedplan;

    private ProfilePage curr;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mtoggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        button_joinedplan = findViewById(R.id.button_joinplan);
        button_myplan = findViewById(R.id.button_myplan);

        //-- Drawer button --
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout_profilepage);
        mtoggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //-- Drawer Button --


        //----Navigation Drawer---

        Intent i = new Intent(this, MapsActivity.class);
        curr = this;

NavigationView navigationView = (NavigationView) findViewById(R.id.nav_bar_profile);
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

    return true;
    }

    });

        //----Navigation Drawer---


        button_myplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(ProfilePage.this);
                builderSingle.setIcon(R.drawable.ic_launcher_background);
                builderSingle.setTitle("Select One Name:-");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ProfilePage.this, android.R.layout.select_dialog_singlechoice);

                arrayAdapter.add("Hardik");
                arrayAdapter.add("Archit");
                arrayAdapter.add("Jignesh");
                arrayAdapter.add("Umang");
                arrayAdapter.add("Gatti");

                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String strName = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(ProfilePage.this);
                        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                dialog.dismiss();
                            }
                        });
                    }
                });
                builderSingle.show();

            }
        });




        button_joinedplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(ProfilePage.this).create(); //Read Update
                alertDialog.setTitle("Joined Plan");
                alertDialog.setMessage("This Alert box displays plans that I have joined in");

                alertDialog.setButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // here you can add functions
                    }
                });

                alertDialog.show();  //<-- See This!
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





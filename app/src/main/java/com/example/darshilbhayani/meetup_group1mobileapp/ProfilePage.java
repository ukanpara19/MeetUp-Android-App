package com.example.darshilbhayani.meetup_group1mobileapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
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
     startActivity(i);
 }

    return true;
    }

    });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mtoggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
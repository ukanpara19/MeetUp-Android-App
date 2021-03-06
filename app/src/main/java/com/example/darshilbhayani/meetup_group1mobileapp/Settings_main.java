package com.example.darshilbhayani.meetup_group1mobileapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Settings_main extends AppCompatActivity {

    private Settings_main curr;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mtoggle;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_main);

        String[] setting = {"Change Password" , "Like us on Facebook" , "About Application"};
        ArrayAdapter setting_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,setting);
        ListView settings_listView = (ListView) findViewById(R.id.setting_lv);
        settings_listView.setAdapter(setting_adapter);

        //-- Drawer button --
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout_settingsMain);
        mtoggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //-- Drawer Button --

        //----Navigation Drawer---

        Intent i = new Intent(this, MapsActivity.class);
        curr = this;

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_bar_settings);
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


        settings_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               if (position == 0){
                   Intent i = new Intent(curr,ChangePassword.class);
                   startActivity(i);
               }
                else if(position == 1){
                   Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                   String facebookUrl = getFacebookPageURL(Settings_main.this);
                   facebookIntent.setData(Uri.parse(facebookUrl));
                   startActivity(facebookIntent);
               }
               else if (position == 2) {
                    AlertDialog alertDialog = new AlertDialog.Builder(Settings_main.this).create(); //Read Update
                    alertDialog.setTitle("About Meetup");
                    alertDialog.setMessage("MeetUp is developed by hardworking Students at Santa Clara University." +"\n"+"\n" + "Out of 4 highly skilled members of awesome team"
                            +"\n"+"\n"+
                     "- The Backend devs are : Darshil Bhiyani , Dhaval Pujara" +"\n"+"\n"+" - Frontend UI devs are : Parth Ladlani, Utsav Kanpara");

                    alertDialog.setButton("Done", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // here you can add functions
                        }
                    });

                    alertDialog.show();  //<-- See This!
                }
            }

        });
    }

    public String getFacebookPageURL(Context context) {
        String FACEBOOK_URL = "https://www.facebook.com/MeetUp-COEN-268-605091763191601/";
        String FACEBOOK_PAGE_ID = "605091763191601";
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
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

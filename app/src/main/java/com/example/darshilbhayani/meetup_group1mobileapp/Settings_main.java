package com.example.darshilbhayani.meetup_group1mobileapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Settings_main extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_main);

        String[] setting = {"Change Password" , "Like us on Facebook" , "About Application"};
        ArrayAdapter setting_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,setting);
        ListView settings_listView = (ListView) findViewById(R.id.setting_lv);
        settings_listView.setAdapter(setting_adapter);

        settings_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (position == 2) {
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
            }

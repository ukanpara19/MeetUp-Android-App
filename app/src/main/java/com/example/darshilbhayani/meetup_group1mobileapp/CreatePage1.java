package com.example.darshilbhayani.meetup_group1mobileapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

public class CreatePage1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_plan1);

        GridLayout grid = (GridLayout) findViewById(R.id.grid_layout_id);
        int childCount = grid.getChildCount();


        for (int i= 0; i < childCount; i++){
            final CardView container = (CardView) grid.getChildAt(i);
            Log.d("View",i+"");
            container.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    // your click code here
                    Log.d("Id", view.getId()+"");
                }
            });
        }
    }
}

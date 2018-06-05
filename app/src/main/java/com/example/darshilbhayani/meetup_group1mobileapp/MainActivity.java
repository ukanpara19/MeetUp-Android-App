package com.example.darshilbhayani.meetup_group1mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private SliderAdapter sliderAdapter;
    private TextView[] mDots;

    private Button btn_next;
    private int mCurrentPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.dots_layout);
        btn_next = (Button) findViewById(R.id.button_next);

        sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndidcator(0);


        mSlideViewPager.addOnPageChangeListener(viewListener);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_next.getText().toString()=="Finish"){
                    Intent intent = new Intent(MainActivity.this,LoginDemo.class);
                    startActivity(intent);
                    finish();
                }
                mSlideViewPager.setCurrentItem(mCurrentPage + 1);
            }
        });
    }

    public void addDotsIndidcator (int position)
    {

        mDots = new TextView[3];
        mDotLayout.removeAllViews();

        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));

            mDotLayout.addView(mDots[i]);

        }

        if (mDots.length > 0 ){
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndidcator(i);
            mCurrentPage = i;
            if (i == 0){
                btn_next.setEnabled(true);
            }
            else if (i == mDots.length -1 ){
                btn_next.setEnabled(true);
                btn_next.setText("Finish");
            }
            else{
                btn_next.setEnabled(true);
                btn_next.setText("Next");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}

package com.example.concertmate;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        TabFragment tabFragment= TabFragment.newInstance(); //get a new Fragment instance
        getSupportFragmentManager().beginTransaction()
                .add(R.id.mainFragment, tabFragment)
                .commit(); // add it to the current activity
    }

}

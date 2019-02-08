package com.example.concertmate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
    }


    public void onCancelButton(View view){
        Intent intent = new Intent(this,TabFragment.class);
        finish();
        startActivity(intent);
    }

    public void onFilterButton(View view){
    }
}

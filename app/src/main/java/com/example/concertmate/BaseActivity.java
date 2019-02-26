package com.example.concertmate;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.concertmate.Fragments.BaseFragment;
import java.util.Calendar;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BaseFragment baseFragment = new BaseFragment();
        super.onCreate(savedInstanceState);
        Calendar c = Calendar.getInstance();
        SharedPreferences pref =getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();

        setContentView(R.layout.activity_base);
        baseFragment.initializeFragments();
        baseFragment.tabFragment(this);
    }

}

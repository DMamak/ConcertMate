package com.example.concertmate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.concertmate.Fragments.BaseFragment;

public class MainLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseFragment baseFragment = new BaseFragment();
        setContentView(R.layout.activity_main_login);
        baseFragment.loginFragment(this);
    }
}

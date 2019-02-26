package com.example.concertmate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.concertmate.Fragments.BaseFragment;
import com.example.concertmate.Fragments.TabFragment;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        BaseFragment.initializeFragments();
        BaseFragment.tabFragment(this);
    }

}

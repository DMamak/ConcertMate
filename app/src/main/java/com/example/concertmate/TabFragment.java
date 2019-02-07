package com.example.concertmate;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.example.concertmate.Adapters.ViewPagerAdapter;


public class TabFragment extends Base {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TabLayout tabLayout;
        ViewPager viewPager;

        setContentView(R.layout.tab_fragment);
        viewPager =findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout =findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }
}

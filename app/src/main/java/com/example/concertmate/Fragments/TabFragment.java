package com.example.concertmate.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.concertmate.Adapters.ViewPagerAdapter;
import com.example.concertmate.R;


public class TabFragment extends BaseFragment {
    int position =-1;

    public static TabFragment newInstance() {
        TabFragment fragment = new TabFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState){
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            position = bundle.getInt("position",0);
        }
        View view = inflater.inflate(R.layout.tab_fragment,container,false);
        TabLayout tabLayout;
        ViewPager viewPager;
        viewPager =view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout =view.findViewById(R.id.tabs);
        if(position != -1) {
            tabLayout.setScrollPosition(position,0f,true);
            viewPager.setCurrentItem(position);
        }
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(upcomingConcertFragment, "Concert");
        adapter.addFragment(concertFragment, "Favorite");
        viewPager.setAdapter(adapter);
    }
}

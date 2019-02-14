package com.example.concertmate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.concertmate.Adapters.ViewPagerAdapter;


public class TabFragment extends BaseFragment {

    public static TabFragment newInstance() {
        TabFragment fragment = new TabFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tab_fragment,container,false);
        TabLayout tabLayout;
        ViewPager viewPager;
        viewPager =view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout =view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new ConcertFragment(), "Concert");
        viewPager.setAdapter(adapter);
    }
}

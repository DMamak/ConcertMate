package com.example.concertmate.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.example.concertmate.Adapters.ViewPagerAdapter;
import com.example.concertmate.Models.Concert;
import com.example.concertmate.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;

public class SingleEventFragment extends BaseFragment {
    CheckBox isFav;
    ImageView bandImage;

    public SingleEventFragment(){}

    public static SingleEventFragment newInstance() {

        return  new SingleEventFragment();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.single_event_fragment,container,false);
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString("concertObj", "");
        Concert concert = gson.fromJson(json, Concert.class);
        isFav = view.findViewById(R.id.single_like_icon);
        isFav.setChecked(concert.isFavorite());
        bandImage = view.findViewById(R.id.single_band_picture);
        Picasso.get().load(concert.getImageURL()).fit().into(bandImage);
        TabLayout tabLayout;
        ViewPager viewPager;
        viewPager =view.findViewById(R.id.single_viewpager);
        setupViewPager(viewPager,concert.isFavorite());
        tabLayout =view.findViewById(R.id.single_tabs);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    private void setupViewPager(ViewPager viewPager,boolean isFav) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
//        adapter.addFragment(new ConcertFragment(), "Concert");
//        adapter.addFragment(new ConcertFragment(), "Favorite");
//        if(isFav){
//            adapter.addFragment(new ConcertFragment(),"Notes");
//        }
        viewPager.setAdapter(adapter);
    }
}

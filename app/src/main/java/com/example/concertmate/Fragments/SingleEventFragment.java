package com.example.concertmate.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import com.example.concertmate.Adapters.ViewPagerAdapter;
import com.example.concertmate.Models.Concert;
import com.example.concertmate.R;
import com.example.concertmate.Utils.TinyDB;
import com.squareup.picasso.Picasso;
import static android.view.View.VISIBLE;

public class SingleEventFragment extends BaseFragment {
    CheckBox isFav;
    ImageView bandImage;
    Button notesButton,returnButton;

    public SingleEventFragment(){}

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.single_event_fragment,container,false);
        Concert concert = getJsonConcert(getContext());

        isFav = view.findViewById(R.id.single_like_icon);
        notesButton=view.findViewById(R.id.add_notes_button);
        returnButton=view.findViewById(R.id.return_button);
        isFav.setChecked(concert.isFavorite());
        if(concert.isFavorite()){
            notesButton.setVisibility(VISIBLE);
        }
        notesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditNote(-1);
            }
        });
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
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
        adapter.addFragment(concertInformationFragment, "Information");
        adapter.addFragment(venueInformationFragment, "Venue");
        if(isFav){
            adapter.addFragment(notesFragment,"Notes");
        }
        viewPager.setAdapter(adapter);
    }
}

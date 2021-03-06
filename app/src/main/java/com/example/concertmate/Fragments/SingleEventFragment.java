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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import static android.view.View.VISIBLE;

public class SingleEventFragment extends BaseFragment {
    CheckBox isFav,attending;
    ImageView bandImage;
    Button notesButton;
    Concert concert;
    DatabaseReference mDatabase;
    FirebaseAuth auth;

    public SingleEventFragment() {
    }

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.single_event_fragment, container, false);
        concert = getJsonConcert(getContext());

        isFav = view.findViewById(R.id.single_like_icon);
        attending=view.findViewById(R.id.single_attend_icon);
        notesButton = view.findViewById(R.id.add_notes_button);
        isFav.setChecked(concert.isFavorite());
        if (concert.isFavorite()) {
            notesButton.setVisibility(VISIBLE);
        }

        attending.setChecked(concert.isAttending());
        if (concert.isAttending()) {
            notesButton.setVisibility(VISIBLE);
        }

        isFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFav.isChecked()) {
                    concert.setFavorite(true);
                    mDatabase.child("concert").child(auth.getCurrentUser().getUid()).child(concert.getId()).setValue(concert);
                } else {
                    if (concert.isAttending()) {
                        concert.setFavorite(false);
                        mDatabase.child("concert").child(auth.getCurrentUser().getUid()).child(concert.getId()).setValue(concert);
                    } else {
                        mDatabase.child("concert").child(auth.getCurrentUser().getUid()).child(concert.getId()).removeValue();
                    }
                }
            }
        });

        attending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attending.isChecked()) {
                    concert.setAttending(true);
                    mDatabase.child("concert").child(auth.getCurrentUser().getUid()).child(concert.getId()).setValue(concert);
                } else {
                    if (concert.isFavorite()) {
                        concert.setAttending(false);
                        mDatabase.child("concert").child(auth.getCurrentUser().getUid()).child(concert.getId()).setValue(concert);
                    } else {
                        mDatabase.child("concert").child(auth.getCurrentUser().getUid()).child(concert.getId()).removeValue();
                    }
                }
            }
        });
        notesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditNote(-1);
            }
        });
        bandImage = view.findViewById(R.id.single_band_picture);
        Picasso.get().load(concert.getImageURL()).fit().into(bandImage);
        TabLayout tabLayout;
        ViewPager viewPager;
        viewPager = view.findViewById(R.id.single_viewpager);
        setupViewPager(viewPager, concert.isFavorite());
        tabLayout = view.findViewById(R.id.single_tabs);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    private void setupViewPager(ViewPager viewPager, boolean isFav) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(concertInformationFragment(), "Information");
        adapter.addFragment(venueInformationFragment(), "Venue");
        if (isFav) {
            adapter.addFragment(notesFragment(), "Notes");
        }
        viewPager.setAdapter(adapter);
    }
}

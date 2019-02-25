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
import com.squareup.picasso.Picasso;
import static android.view.View.VISIBLE;

public class SingleEventFragment extends BaseFragment {
    CheckBox isFav;
    ImageView bandImage;
    Button notesButton;

    public SingleEventFragment(){}

    public static SingleEventFragment newInstance() {

        return  new SingleEventFragment();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.single_event_fragment,container,false);
        Concert concert = getJsonConcert();
        isFav = view.findViewById(R.id.single_like_icon);
        notesButton=view.findViewById(R.id.add_notes_button);
        isFav.setChecked(concert.isFavorite());
        if(concert.isFavorite()){
            notesButton.setVisibility(VISIBLE);
        }
        notesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditNotesFragment editNotesFragment = EditNotesFragment.newInstance(); //get a new Fragment instance
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainFragment, editNotesFragment)
                        .addToBackStack(null)
                        .commit();
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
        adapter.addFragment(new ConcertInformationFragment(), "Information");
        adapter.addFragment(new VenueInformationFragment(), "Venue");
        if(isFav){
            adapter.addFragment(new NotesFragment(),"Notes");
        }
        viewPager.setAdapter(adapter);
    }
}

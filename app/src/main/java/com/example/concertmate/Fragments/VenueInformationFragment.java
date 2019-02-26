package com.example.concertmate.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.concertmate.Models.Venue;
import com.example.concertmate.R;


public class VenueInformationFragment extends BaseFragment {

    public VenueInformationFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.venue_information_fragment, container, false);
        TextView venueName = view.findViewById(R.id.single_venue_name);
        TextView address = view.findViewById(R.id.single_venue_address);
        TextView postcode = view.findViewById(R.id.single_venue_postcode);
        TextView parking = view.findViewById(R.id.single_venue_parking);
        TextView accessible = view.findViewById(R.id.single_venue_accessible);
        Venue venue = getJsonConcert(getContext()).getVenue();

        venueName.setText(venue.getVenueName());
        address.setText(venue.getAddress());
        postcode.setText(venue.getPostCode());
        parking.setText(venue.getParking());
        accessible.setText(venue.getAccessible());
        return view;
    }
}

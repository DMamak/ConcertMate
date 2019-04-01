package com.example.concertmate.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.concertmate.Models.Venue;
import com.example.concertmate.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class MarkerInfoAdapter implements GoogleMap.InfoWindowAdapter {
    private Marker markerShowingInfoWindow;
    RecyclerView mapRecycler;
    private final View mWindow;

    public MarkerInfoAdapter(Activity context,RecyclerView mapRecycler) {
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_marker_window, null);
        this.mapRecycler=mapRecycler;
    }

    private void renderWindowText(Marker marker, View view) {
        markerShowingInfoWindow = marker;
        final Venue venue = (Venue) marker.getTag();
        final ImageView venueImage = view.findViewById(R.id.window_venue_image);
        final TextView venueName = view.findViewById(R.id.window_venue_name);
        Picasso.get().load(venue.getUrl()).placeholder(R.drawable.image_not_available)
                .resize(400, 200).centerCrop().into(venueImage, onImageLoaded);

        venueName.setText(venue.getVenueName());
    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker, mWindow);

        return mWindow;
    }


    private Callback onImageLoaded = new Callback() {

        @Override
        public void onSuccess() {
            if (markerShowingInfoWindow != null && markerShowingInfoWindow.isInfoWindowShown()) {
                markerShowingInfoWindow.hideInfoWindow();
                markerShowingInfoWindow.showInfoWindow();
                mapRecycler.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onError(Exception e) {
            Log.e("ERROR", e.toString());
        }

    };

}

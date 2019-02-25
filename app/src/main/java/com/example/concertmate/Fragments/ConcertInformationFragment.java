package com.example.concertmate.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.concertmate.Fragments.BaseFragment;
import com.example.concertmate.Models.Concert;
import com.example.concertmate.R;
import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;

import static android.content.Context.MODE_PRIVATE;

public class ConcertInformationFragment extends BaseFragment {

    public ConcertInformationFragment() {
        // Required empty public constructor
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_concert_information, container, false);
        final Concert concert = getJsonConcert();
        TextView name = view.findViewById(R.id.single_concert_name);
        TextView date = view.findViewById(R.id.single_concert_date);
        TextView time = view.findViewById(R.id.single_concert_time);
        TextView genre = view.findViewById(R.id.single_concert_genre);
        TextView subGenre = view.findViewById(R.id.single_concert_sub_genre);
        ImageButton youtube = view.findViewById(R.id.youtube_button);
        ImageButton facebook = view.findViewById(R.id.facebook_button);
        ImageButton twitter = view.findViewById(R.id.twitter_button);
        name.setText(concert.getName());
        date.setText(concert.getDate());
        time.setText(concert.getTime());
        genre.setText(concert.getGenre());
        subGenre.setText(concert.getSubGenre());

        if(!StringUtils.isNotBlank(concert.getYoutubeLink())){
            youtube.setVisibility(View.INVISIBLE);
        }
        if(!StringUtils.isNotBlank(concert.getFacebookLink())){
            facebook.setVisibility(View.INVISIBLE);
        }
        if(!StringUtils.isNotBlank(concert.getTwitterLink())){
            twitter.setVisibility(View.INVISIBLE);
        }
        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(concert.getYoutubeLink()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(concert.getFacebookLink()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(concert.getTwitterLink()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        return view;
    }

}


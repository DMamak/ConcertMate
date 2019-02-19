package com.example.concertmate.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.concertmate.Models.Concert;
import com.example.concertmate.R;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class SingleEventFragment extends BaseFragment {

    public SingleEventFragment(){}

    public static SingleEventFragment newInstance() {
        SingleEventFragment fragment = new SingleEventFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.single_event_fragment,container,false);
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        TextView chujeView = view.findViewById(R.id.chujeView);
        Gson gson = new Gson();
        String json = pref.getString("concertObj", "");
        Concert concert = gson.fromJson(json, Concert.class);
        chujeView.setText(concert.getName());
        return view;
    }
}

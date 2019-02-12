package com.example.concertmate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FilterFragment extends Fragment {

    public FilterFragment() {
    }

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.filterFragment,container,false);


        return view;
    }


    public void onCancelButton(View view) {
    }

    public void onFilterButton(View view){
    }
}

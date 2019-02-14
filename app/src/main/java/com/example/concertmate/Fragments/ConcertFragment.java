package com.example.concertmate.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.concertmate.Adapters.concertAdapterView;
import com.example.concertmate.R;

import java.util.Calendar;


public class ConcertFragment extends BaseFragment {
    concertAdapterView adapter= new concertAdapterView(concertList);
    public ConcertFragment() {
    }
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState){
        Calendar c = Calendar.getInstance();
        View view = inflater.inflate(R.layout.recycler_fragment,container,false);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterFragment filterFragment = FilterFragment.newInstance(); //get a new Fragment instance
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainFragment, filterFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        Bundle bundle = this.getArguments();
        int check = bundle.getInt("position");
        RecyclerView mRecycler = view.findViewById(R.id.recycler);
        mRecycler.setAdapter(adapter);
        ticketmasterApiRequest(getActivity().getApplicationContext(),adapter,c);
        RecyclerView.LayoutManager mLayoutManager =new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(mLayoutManager);
        return view;
    }
}

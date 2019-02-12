package com.example.concertmate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.concertmate.Adapters.concertAdapterView;


public class ConcertFragment extends BaseFragment {
    concertAdapterView adapter= new concertAdapterView(concertList);
    public ConcertFragment() {
    }
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.recycler_fragment,container,false);
        Bundle bundle = this.getArguments();
        int check = bundle.getInt("position");
        RecyclerView mRecycler = view.findViewById(R.id.recycler);
        mRecycler.setAdapter(adapter);
        ticketmasterApiRequest(getActivity().getApplicationContext(),adapter);
        RecyclerView.LayoutManager mLayoutManager =new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(mLayoutManager);
        return view;
    }
}

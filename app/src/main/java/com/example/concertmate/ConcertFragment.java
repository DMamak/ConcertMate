package com.example.concertmate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.concertmate.Adapters.concertAdapterView;
import com.example.concertmate.Models.Concert;

import java.util.ArrayList;

public class ConcertFragment extends Fragment {

    public ConcertFragment() {
    }
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.recycler_fragment,container,false);
        Bundle bundle = this.getArguments();
        ArrayList<Concert> arrayList  = bundle.getParcelableArrayList("arraylist");
        RecyclerView mRecycler = view.findViewById(R.id.recycler);
        concertAdapterView adapter = new concertAdapterView(arrayList);
        mRecycler.setAdapter(adapter);
        RecyclerView.LayoutManager mLayoutManager =new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(mLayoutManager);
        adapter.notifyDataSetChanged();
        return view;
    }
}

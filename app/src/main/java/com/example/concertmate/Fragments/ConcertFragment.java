package com.example.concertmate.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import com.example.concertmate.Adapters.FavoriteConcertAdapter;
import com.example.concertmate.Adapters.concertAdapterView;
import com.example.concertmate.Models.Concert;
import com.example.concertmate.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ConcertFragment extends BaseFragment {
    concertAdapterView adapter;
    FavoriteConcertAdapter firebaseRecyclerAdapter;

    public ConcertFragment() {
    }

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState){
        final Bundle bundle = this.getArguments();
        int check = bundle.getInt("position");
        final Bundle newBundle = new Bundle();
        newBundle.putInt("position",check);
        View view = inflater.inflate(R.layout.recycler_fragment,container,false);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             filterFragment(getActivity());
            }
        });

        final SearchView mySearch = view.findViewById(R.id.upcoming_search_view);
        if(check ==0) {
            adapter = new concertAdapterView(concertList,getActivity());
            RecyclerView mRecycler = view.findViewById(R.id.recycler);
            mRecycler.setAdapter(adapter);
            ticketmasterApiRequest(getActivity().getApplicationContext(), adapter,"");
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mRecycler.setLayoutManager(mLayoutManager);
            mySearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    ticketmasterApiRequest(getActivity().getApplicationContext(), adapter,s);
                    mySearch.clearFocus();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                   if(TextUtils.isEmpty(s)){
                       ticketmasterApiRequest(getActivity().getApplicationContext(), adapter,"");
                   }
                    return false;
                }
            });
        }else{
            final Query query;
            query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("concert");

            FirebaseRecyclerOptions<Concert> options =
                    new FirebaseRecyclerOptions.Builder<Concert>()
                            .setQuery(query, Concert.class)
                            .build();

            firebaseRecyclerAdapter = new FavoriteConcertAdapter(options,getActivity());
            firebaseRecyclerAdapter.startListening();
            Log.i("INFO",String.valueOf(concertList.size()));
            RecyclerView mRecycler = view.findViewById(R.id.recycler);
            mRecycler.setAdapter(firebaseRecyclerAdapter);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mRecycler.setLayoutManager(mLayoutManager);
            mySearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    firebaseRecyclerAdapter.getFilter().filter(s);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    firebaseRecyclerAdapter.getFilter().filter(s);
                    return false;
                }
            });
                    }

        return view;
    }
}

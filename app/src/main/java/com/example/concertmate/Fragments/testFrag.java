package com.example.concertmate.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.concertmate.Adapters.FavoriteConcertAdapter;
import com.example.concertmate.Models.Concert;
import com.example.concertmate.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class testFrag extends BaseFragment {
    FavoriteConcertAdapter firebaseRecyclerAdapter;

    public testFrag() {

    }

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_frag, container, false);
//        final SearchView mySearch = view.findViewById(R.id.upcoming_search_view);

        final Query query;
        query = FirebaseDatabase.getInstance()
                .getReference()
                .child("concert");

        FirebaseRecyclerOptions<Concert> options =
                new FirebaseRecyclerOptions.Builder<Concert>()
                        .setQuery(query, Concert.class)
                        .build();

        firebaseRecyclerAdapter = new FavoriteConcertAdapter(options);
        firebaseRecyclerAdapter.startListening();

        RecyclerView mRecycler = view.findViewById(R.id.test_recycler);
        mRecycler.setAdapter(firebaseRecyclerAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(mLayoutManager);
//        mySearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                firebaseRecyclerAdapter.getFilter().filter(s);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                firebaseRecyclerAdapter.getFilter().filter(s);
//                return false;
//            }
//        });

        return view;
    }
}

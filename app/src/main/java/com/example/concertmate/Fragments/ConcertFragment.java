package com.example.concertmate.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.concertmate.Adapters.RecyclerItemClickListener;
import com.example.concertmate.Adapters.concertAdapterView;
import com.example.concertmate.R;
import com.example.concertmate.Utils.ConcertFilter;
import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;


public class ConcertFragment extends BaseFragment {
    concertAdapterView adapter= new concertAdapterView(concertList);
    public ConcertFilter concertFilter;

    public ConcertFragment() {
    }
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState){
        final Calendar c = Calendar.getInstance();
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
        final SearchView mySearch = view.findViewById(R.id.upcoming_search_view);
        Bundle bundle = this.getArguments();
        int check = bundle.getInt("position");
        if(check ==0) {
            RecyclerView mRecycler = view.findViewById(R.id.recycler);
            mRecycler.setAdapter(adapter);
            ticketmasterApiRequest(getActivity().getApplicationContext(), adapter, c,"");
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mRecycler.setLayoutManager(mLayoutManager);
            mySearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    ticketmasterApiRequest(getActivity().getApplicationContext(), adapter, c,s);
                    mySearch.clearFocus();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    //TODO clearFocus does not work as intented, waiting fot answer from stackOverflow,
                   if(!StringUtils.isNotBlank(s)){
                       ticketmasterApiRequest(getActivity().getApplicationContext(), adapter, c,"");
                       mySearch.clearFocus();
                   }
                    return false;
                }
            });

//            mRecycler.addOnItemTouchListener(
//                    new RecyclerItemClickListener(getContext(), mRecycler ,new RecyclerItemClickListener.OnItemClickListener() {
//                        @Override public void onItemClick(View view, int position) {
//
//                            SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
//                            SharedPreferences.Editor editor = pref.edit();
//                            Gson gson = new Gson();
//                            String json = gson.toJson(concertList.get(position));
//                            editor.putString("concertObj", json);
//                            editor.apply();
//                            SingleEventFragment singleEventFragment = SingleEventFragment.newInstance();
//                            getActivity().getSupportFragmentManager().beginTransaction()
//                                    .replace(R.id.mainFragment, singleEventFragment)
//                                    .addToBackStack(null)
//                                    .commit();
//                        }
//
//
//                        @Override public void onLongItemClick(View view, int position) {
//                            // do whatever
//                        }
//                    })
//            );

        }else{
            concertFilter = new ConcertFilter(concertList,adapter);
            mySearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    concertFilter.filter(s);
                    return false;
                }
            });
        }

        return view;
    }
}

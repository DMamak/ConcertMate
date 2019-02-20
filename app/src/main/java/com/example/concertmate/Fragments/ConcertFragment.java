package com.example.concertmate.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.concertmate.Adapters.RecyclerItemClickListener;
import com.example.concertmate.Adapters.concertAdapterView;
import com.example.concertmate.Models.Concert;
import com.example.concertmate.R;
import com.example.concertmate.Utils.ConcertFilter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;


public class ConcertFragment extends BaseFragment {
    concertAdapterView adapter= new concertAdapterView(concertList);
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    public ConcertFilter concertFilter;
    private DatabaseReference mDatabase;

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
            final Query query;
            query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("concert");
            mySearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {

                    return false;
                }
            });



            FirebaseRecyclerOptions<Concert> options =
                    new FirebaseRecyclerOptions.Builder<Concert>()
                            .setQuery(query, Concert.class)
                            .build();

            firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Concert, recyclerViewHolder>(options) {
                @Override
                public recyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.concert_card_view, parent, false);

                    return new recyclerViewHolder(view);
                }

                @Override
                protected void onBindViewHolder(@Nullable recyclerViewHolder holder, int position, @Nullable final Concert concert) {
                    final CheckBox box = holder.box;
                    holder.name.setText(concert.getName());
                    holder.date.setText(concert.getDate());
                    holder.venue.setText(concert.getVenue().getVenueName());
                    Picasso.get().load(concert.getImageURL()).fit().into(holder.picture);
                    holder.box.setChecked(concert.isFavorite());
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    box.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!box.isChecked()) {
                                mDatabase.child("concert").child(concert.getId()).removeValue();
                            }
                        }
                    });

            }};
            firebaseRecyclerAdapter.startListening();

            RecyclerView mRecycler = view.findViewById(R.id.recycler);
            mRecycler.setAdapter(firebaseRecyclerAdapter);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mRecycler.setLayoutManager(mLayoutManager);
                    }

        return view;
    }
    class recyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView date;
        private TextView venue;
        private ImageView picture;
        private CheckBox box;

        private recyclerViewHolder(View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.band_picture);
            name = itemView.findViewById(R.id.band_name);
            date = itemView.findViewById(R.id.concert_date);
            venue = itemView.findViewById(R.id.venue_name);
            box = itemView.findViewById(R.id.like_icon);
        }
    }
}

package com.example.concertmate.Adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.concertmate.Fragments.BaseFragment;
import com.example.concertmate.Models.Concert;
import com.example.concertmate.R;
import com.example.concertmate.Utils.TinyDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class concertAdapterView extends RecyclerView.Adapter<concertAdapterView.myViewHolder> {
    public List<Concert> concertList;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private FragmentActivity activity;


    public concertAdapterView(List<Concert> concertList, FragmentActivity activity) {
        this.concertList = concertList;
        this.activity = activity;

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.concert_card_view, viewGroup, false);
        return new concertAdapterView.myViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder viewHolder, int i) {
        final CheckBox box = viewHolder.box;
        final CheckBox attend = viewHolder.attending;
        final Concert concert = concertList.get(i);
        viewHolder.name.setText(concert.getName());
        viewHolder.date.setText(concert.getDate());
        viewHolder.venue.setText(concert.getVenue().getVenueName());
        Picasso.get().load(concert.getImageURL()).fit().into(viewHolder.picture);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (box.isChecked()) {
                    concert.setFavorite(true);
                    mDatabase.child("concert").child(auth.getCurrentUser().getUid()).child(concert.getId()).setValue(concert);
                } else {
                    if (concert.isAttending()) {
                        concert.setFavorite(false);
                        mDatabase.child("concert").child(auth.getCurrentUser().getUid()).child(concert.getId()).setValue(concert);
                    } else {
                        mDatabase.child("concert").child(auth.getCurrentUser().getUid()).child(concert.getId()).removeValue();
                    }
                }
            }
        });

        attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attend.isChecked()) {
                    concert.setAttending(true);
                    mDatabase.child("concert").child(auth.getCurrentUser().getUid()).child(concert.getId()).setValue(concert);
                } else {
                    if (concert.isFavorite()) {
                        concert.setAttending(false);
                        mDatabase.child("concert").child(auth.getCurrentUser().getUid()).child(concert.getId()).setValue(concert);
                    } else {
                        mDatabase.child("concert").child(auth.getCurrentUser().getUid()).child(concert.getId()).removeValue();
                    }
                }
            }
        });

        mDatabase.child("concert").child(auth.getCurrentUser().getUid()).child(concert.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for(DataSnapshot data :dataSnapshot.getChildren()){
                                if((StringUtils.equalsIgnoreCase(data.getKey(),"favorite"))
                                        &&(StringUtils.equalsIgnoreCase(data.getValue().toString(),"true"))){
                                    box.setChecked(true);
                                    concert.setFavorite(true);

                                }else if ((StringUtils.equalsIgnoreCase(data.getKey(),"favorite"))
                                        &&(StringUtils.equalsIgnoreCase(data.getValue().toString(),"false"))){
                                    box.setChecked(false);
                                    concert.setFavorite(false);
                                }

                                if((StringUtils.equalsIgnoreCase(data.getKey(),"attending"))
                                        &&(StringUtils.equalsIgnoreCase(data.getValue().toString(),"true"))){
                                    attend.setChecked(true);
                                    concert.setAttending(true);

                                }else if ((StringUtils.equalsIgnoreCase(data.getKey(),"attending"))
                                        &&(StringUtils.equalsIgnoreCase(data.getValue().toString(),"false"))){
                                    attend.setChecked(false);
                                    concert.setAttending(false);
                                }
                            }

                        } else {
                            box.setChecked(false);
                            concert.setFavorite(false);
                            attend.setChecked(false);
                            concert.setAttending(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        viewHolder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TinyDB tinydb = new TinyDB(activity);
                tinydb.putObject("concertObj", concert);
                BaseFragment.singleEventFragment(activity);
            }
        });

    }

    @Override
    public int getItemCount() {
        return concertList.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView date;
        private TextView venue;
        private ImageView picture;
        private CheckBox box;
        private CheckBox attending;

        private myViewHolder(View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.band_picture);
            name = itemView.findViewById(R.id.band_name);
            date = itemView.findViewById(R.id.concert_date);
            venue = itemView.findViewById(R.id.venue_name);
            box = itemView.findViewById(R.id.like_icon);
            attending = itemView.findViewById(R.id.attend_icon);
        }
    }


}
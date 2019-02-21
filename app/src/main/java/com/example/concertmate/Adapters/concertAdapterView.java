package com.example.concertmate.Adapters;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.concertmate.Fragments.SingleEventFragment;
import com.example.concertmate.Models.Concert;
import com.example.concertmate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class concertAdapterView extends RecyclerView.Adapter<concertAdapterView.myViewHolder> {
    public List<Concert> concertList;
    private DatabaseReference mDatabase;
    private FragmentActivity activity;


    public concertAdapterView(List<Concert> concertList, FragmentActivity activity) {
        this.concertList = concertList;
        this.activity=activity;

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
        final Concert concert = concertList.get(i);
        viewHolder.name.setText(concert.getName());
        viewHolder.date.setText(concert.getDate());
        viewHolder.venue.setText(concert.getVenue().getVenueName());
        Picasso.get().load(concert.getImageURL()).fit().into(viewHolder.picture);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (box.isChecked()) {
                    concert.setFavorite(true);
                    mDatabase.child("concert").child(concert.getId()).setValue(concert);
                } else {
                    concert.setFavorite(false);
                    mDatabase.child("concert").child(concert.getId()).removeValue();
                }
            }
        });
        mDatabase.child("concert").child(concert.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            box.setChecked(true);
                            concert.setFavorite(true);
                        } else {
                            box.setChecked(false);
                            concert.setFavorite(false);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        viewHolder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = v.getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                Gson gson = new Gson();
                String json = gson.toJson(concert);
                editor.putString("concertObj", json);
                editor.apply();
                SingleEventFragment singleEventFragment = SingleEventFragment.newInstance();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainFragment, singleEventFragment)
                        .addToBackStack(null)
                        .commit();
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

        private myViewHolder(View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.band_picture);
            name = itemView.findViewById(R.id.band_name);
            date = itemView.findViewById(R.id.concert_date);
            venue = itemView.findViewById(R.id.venue_name);
            box = itemView.findViewById(R.id.like_icon);
        }
    }


}
package com.example.concertmate.Adapters;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.concertmate.Adapters.FirebaseCustomAdapters.FirebaseRecyclerAdapter;
import com.example.concertmate.Fragments.BaseFragment;
import com.example.concertmate.Models.Concert;
import com.example.concertmate.R;
import com.example.concertmate.Utils.TinyDB;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AttendingConcertAdapter extends FirebaseRecyclerAdapter<Concert, AttendingConcertAdapter.AttendingConcertViewHolder> {
    private FragmentActivity activity;
    private RecycleItemClick recycleItemClick;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;

    public AttendingConcertAdapter(FirebaseRecyclerOptions<Concert> options) {
        super(options, true);
    }

    public AttendingConcertAdapter(FirebaseRecyclerOptions<Concert> options, FragmentActivity activity) {
        super(options, true);
        this.activity = activity;
    }

    public interface RecycleItemClick {
        void onItemClick(String userId, Concert user, int position);
    }

    public void setRecycleItemClick(RecycleItemClick recycleItemClick) {
        this.recycleItemClick = recycleItemClick;
    }

    @Override
    public AttendingConcertViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.concert_card_view, parent, false);

        return new AttendingConcertViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(AttendingConcertViewHolder holder, int position, final Concert concert) {
        if(!concert.isAttending() && concert.isFavorite()) {
            holder.itemView.setVisibility(View.GONE);
        }else{
            holder.itemView.setVisibility(View.VISIBLE);
        }
        final CheckBox box = holder.box;
        final CheckBox attend = holder.attending;
        attend.setChecked(concert.isAttending());
        box.setChecked(concert.isFavorite());
        holder.name.setText(concert.getName());
        holder.date.setText(concert.getDate());
        holder.venue.setText(concert.getVenue().getVenueName());
        Picasso.get().load(concert.getImageURL()).fit().into(holder.picture);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!box.isChecked()) {
                    if (concert.isAttending()) {
                        concert.setFavorite(false);
                        mDatabase.child("concert").child(auth.getCurrentUser().getUid()).child(concert.getId()).setValue(concert);
                    } else {
                        concert.setFavorite(false);
                        mDatabase.child("concert").child(auth.getCurrentUser().getUid()).child(concert.getId()).removeValue();
                    }
                }else{
                    concert.setFavorite(true);
                    mDatabase.child("concert").child(auth.getCurrentUser().getUid()).child(concert.getId()).setValue(concert);
                }
            }
        });

        attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!attend.isChecked()) {
                    if (concert.isFavorite()) {
                        concert.setAttending(false);
                        mDatabase.child("concert").child(auth.getCurrentUser().getUid()).child(concert.getId()).setValue(concert);
                    } else {
                        concert.setAttending(false);
                        mDatabase.child("concert").child(auth.getCurrentUser().getUid()).child(concert.getId()).removeValue();
                    }
                }else{
                    concert.setAttending(true);
                    mDatabase.child("concert").child(auth.getCurrentUser().getUid()).child(concert.getId()).setValue(concert);
                }
            }
        });


        holder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TinyDB tinydb = new TinyDB(activity);
                tinydb.putObject("concertObj", concert);
                BaseFragment.singleEventFragment(activity);
            }
        });
    }



    @Override
    protected boolean filterCondition(Concert model, String filterPattern) {
        return model.getName().toLowerCase().contains(filterPattern);
    }

    class AttendingConcertViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView date;
        private TextView venue;
        private ImageView picture;
        private CheckBox box;
        private CheckBox attending;

        private AttendingConcertViewHolder(View itemView) {
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

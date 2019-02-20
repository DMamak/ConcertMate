package com.example.concertmate.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.concertmate.Adapters.FirebaseCustomAdapters.FirebaseRecyclerAdapter;
import com.example.concertmate.Models.Concert;
import com.example.concertmate.R;
import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class FavoriteConcertAdapter extends FirebaseRecyclerAdapter<Concert, FavoriteConcertAdapter.ConcertViewHolder> {

    private RecycleItemClick recycleItemClick;
    private DatabaseReference mDatabase;
    private static final String TAG = "PeopleListAdapter";

    public FavoriteConcertAdapter(FirebaseRecyclerOptions<Concert> options) {
        super(options, true);
    }

    public interface RecycleItemClick {
        void onItemClick(String userId, Concert user, int position);
    }

    public void setRecycleItemClick(RecycleItemClick recycleItemClick) {
        this.recycleItemClick = recycleItemClick;
    }

    @Override
    public ConcertViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.concert_card_view, parent, false);

        return new ConcertViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ConcertViewHolder holder, int position, final Concert concert) {
        final CheckBox box = holder.box;
        box.setChecked(concert.isFavorite());
        holder.name.setText(concert.getName());
        holder.date.setText(concert.getDate());
        holder.venue.setText(concert.getVenue().getVenueName());
        Picasso.get().load(concert.getImageURL()).fit().into(holder.picture);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!box.isChecked()) {
                    concert.setFavorite(false);
                    mDatabase.child("concert").child(concert.getId()).removeValue();
                }
            }
        });
    }



    @Override
    protected boolean filterCondition(Concert model, String filterPattern) {
        return model.getName().toLowerCase().contains(filterPattern);
    }

    class ConcertViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView date;
        private TextView venue;
        private ImageView picture;
        private CheckBox box;

        private ConcertViewHolder(View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.band_picture);
            name = itemView.findViewById(R.id.band_name);
            date = itemView.findViewById(R.id.concert_date);
            venue = itemView.findViewById(R.id.venue_name);
            box = itemView.findViewById(R.id.like_icon);
        }
    }
}

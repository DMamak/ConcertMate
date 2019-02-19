package com.example.concertmate.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.concertmate.Models.Concert;
import com.example.concertmate.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class concertAdapterView extends RecyclerView.Adapter<concertAdapterView.myViewHolder>{
    public List<Concert> concertList;

    public concertAdapterView(List<Concert> concertList) {
        this.concertList = concertList;

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.concert_card_view,viewGroup,false);
        return new concertAdapterView.myViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder viewHolder, int i) {
        final CheckBox box=viewHolder.box;
        Concert concert = concertList.get(i);
        viewHolder.name.setText(concert.getName());
        viewHolder.date.setText(concert.getDate());
        viewHolder.venue.setText(concert.getVenue().getVenueName());
        Picasso.get().load(concert.getImageURL()).fit().into(viewHolder.picture);
        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(box.isChecked()){
                    Log.i("INFO","CHECKED");
                }else{
                    Log.i("INFO","UNCHECKED");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return concertList.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView date;
        private TextView venue;
        private ImageView picture;
        private CheckBox box;

        private myViewHolder(View itemView) {
            super(itemView);
            picture=itemView.findViewById(R.id.band_picture);
            name=itemView.findViewById(R.id.band_name);
            date=itemView.findViewById(R.id.concert_date);
            venue=itemView.findViewById(R.id.venue_name);
            box=itemView.findViewById(R.id.likeIcon);
        }
    }
}
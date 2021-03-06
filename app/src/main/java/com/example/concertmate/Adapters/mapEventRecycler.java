package com.example.concertmate.Adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.concertmate.Fragments.BaseFragment;
import com.example.concertmate.Models.Concert;
import com.example.concertmate.R;
import com.example.concertmate.Utils.TinyDB;
import com.squareup.picasso.Picasso;

import java.util.List;

public class mapEventRecycler extends RecyclerView.Adapter<mapEventRecycler.myViewHolder> {
    private List<Concert> concertList;
    private FragmentActivity activity;

    public mapEventRecycler(List<Concert> concertList, FragmentActivity activity) {
        this.concertList = concertList;
        this.activity = activity;

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.map_recycler_layout, viewGroup, false);
        return new mapEventRecycler.myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i) {
        final Concert concert = concertList.get(i);
        myViewHolder.name.setText(concert.getName());
        myViewHolder.date.setText(concert.getDate());
        Picasso.get().load(concert.getImageURL()).placeholder(R.drawable.image_not_available).fit().into(myViewHolder.picture);
        myViewHolder.picture.setOnClickListener(new View.OnClickListener() {
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
        private ImageView picture;
        private myViewHolder(View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.map_concert_image);
            name = itemView.findViewById(R.id.map_concert_name);
            date = itemView.findViewById(R.id.map_concert_date);

        }
    }
}

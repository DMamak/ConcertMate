package com.example.concertmate.Utils;

import android.widget.Filter;

import com.example.concertmate.Adapters.concertAdapterView;
import com.example.concertmate.Models.Concert;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ConcertFilter extends Filter {
    public List<Concert> originalConcertList;
    public concertAdapterView adapter;

    public ConcertFilter(List<Concert> originalConcertList, concertAdapterView adapter) {
        super();
        this.originalConcertList = originalConcertList;
        this.adapter = adapter;
    }
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        List<Concert> newConcerts;
        String concertName;

        if (constraint == null || constraint.length() == 0) {
                results.values = originalConcertList;
                results.count = originalConcertList.size();
        } else {
            String constraintString = constraint.toString().toLowerCase();
            newConcerts = new ArrayList<>();

            for (Concert c : originalConcertList) {
                concertName = c.getName().toLowerCase();
                if (StringUtils.contains(concertName,constraintString)) {
                        newConcerts.add(c);
                    }}
            results.values = newConcerts;
            results.count = newConcerts.size();
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.concertList = (ArrayList<Concert>) results.values;
        if (results.count >= 0)
            adapter.notifyDataSetChanged();
        else {
            adapter.concertList = originalConcertList;
            adapter.notifyDataSetChanged();
        }

    }
}

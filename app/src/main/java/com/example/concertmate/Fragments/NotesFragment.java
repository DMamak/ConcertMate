package com.example.concertmate.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.example.concertmate.Models.Notes;
import com.example.concertmate.R;

import java.util.ArrayList;

public class NotesFragment extends BaseFragment {
    ListView notesListView;

    public NotesFragment(){}

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notes_list, container, false);
        Log.i("INFO",getConcert().getName());

        notesListView = view.findViewById(R.id.notes_list_view);
        notesListView.setOnItemClickListener(new OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //TODO add fragment to view and edit the notes
            }
        });
        CustomAdapter adapter = new CustomAdapter(getContext(), getConcert().getNotesArrayList());
        notesListView.setAdapter(adapter);
        return view;
    }


    private class CustomAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<Notes> data;
        private LayoutInflater inflater;

        public CustomAdapter(Context c, ArrayList<Notes> data) {
            this.context = c;
            this.data = data;
            this.inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return data.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View view, ViewGroup parent) {
            View vi = view;
            if (view == null)
                vi = inflater.inflate(R.layout.custom_listview,parent,false);

            TextView title =vi.findViewById(R.id.notesTextView);

            Notes c = data.get(position);
            title.setText(c.getNotes());
            return vi;
        }

    }
}

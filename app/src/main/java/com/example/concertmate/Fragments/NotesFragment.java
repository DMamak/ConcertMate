package com.example.concertmate.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.example.concertmate.Models.Concert;
import com.example.concertmate.Models.Notes;
import com.example.concertmate.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NotesFragment extends BaseFragment {
    private DatabaseReference mDatabase;
    ListView notesListView;

    public NotesFragment(){}

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notes_list, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        notesListView = view.findViewById(R.id.notes_list_view);
        final  CustomAdapter adapter = new CustomAdapter(getContext(), getJsonConcert().getNotesArrayList());
        notesListView.setOnItemClickListener(new OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putInt("positionOfNote",position);
                goToEditNote(bundle);
            }
        });

        notesListView.setAdapter(adapter);
        notesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        notesListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_item_delete_notes, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_item_delete_notes:
                        deleteNotes(mode);
                        return true;
                    default:
                        return false;
                }
            }

            private void deleteNotes(ActionMode actionMode)
            {
                Concert concert = getJsonConcert();
                for (int i = adapter.getCount() - 1; i >= 0; i--)
                {
                    if (notesListView.isItemChecked(i))
                    {
                        concert.getNotesArrayList().remove(i);
                        mDatabase.child("concert").child(concert.getId()).setValue(concert);
                        adapter.getData().remove(i);
                    }
                }
                actionMode.finish();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
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

        public ArrayList<Notes> getData(){
            return data;
        }

    }
}

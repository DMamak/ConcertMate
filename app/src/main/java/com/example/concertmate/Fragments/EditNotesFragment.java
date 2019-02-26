package com.example.concertmate.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.concertmate.Models.Concert;
import com.example.concertmate.Models.Notes;
import com.example.concertmate.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class EditNotesFragment extends BaseFragment {
    private DatabaseReference mDatabase;
    int index = -1;
    public EditNotesFragment(){}


    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final Concert concert = getJsonConcert(getContext());

        Bundle bundle = this.getArguments();
        if(bundle!=null){
            index = getArguments().getInt("positionOfNote");
        }
        View view = inflater.inflate(R.layout.edit_notes, container, false);
        final TextView txt= view.findViewById(R.id.note_view);
        if(index != -1){
            txt.setText(concert.getNotesArrayList().get(index).getNotes());
        }
        final BottomNavigationView navigation =view.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_cancel:
                        getActivity().getSupportFragmentManager().popBackStack();
                        return true;
                    case R.id.navigation_delete:
                        concert.getNotesArrayList().remove(concert.getNotesArrayList().get(getArguments().getInt("positionOfNote")));
                        mDatabase.child("concert").child(concert.getId()).setValue(concert);
                        saveJsonConcert(concert);
                        getActivity().getSupportFragmentManager().popBackStack();
                        return true;
                    case R.id.navigation_save:
                        ArrayList<Notes> notes = concert.getNotesArrayList();
                        if(index != -1){
                            notes.get(index).setNotes(txt.getText().toString());
                        }else{
                            notes.add(new Notes(txt.getText().toString()));
                        }

                        concert.setNotesArrayList(notes);
                        mDatabase.child("concert").child(concert.getId()).setValue(concert);
                        saveJsonConcert(concert);
                        getActivity().getSupportFragmentManager().popBackStack();
                        return true;
                }
                return false;
            }
        });
        return view;
    }
}

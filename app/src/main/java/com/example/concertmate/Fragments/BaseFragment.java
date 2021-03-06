package com.example.concertmate.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.example.concertmate.Models.Concert;
import com.example.concertmate.R;
import com.example.concertmate.Utils.TinyDB;

import java.util.ArrayList;

public class BaseFragment extends Fragment {
    ArrayList<Concert> concertList = new ArrayList<>();

    public Concert getJsonConcert(Context context) {
        TinyDB tinydb = new TinyDB(context);
        return tinydb.getObject("concertObj", Concert.class);
    }

    public void saveJsonConcert(Concert concert) {
        TinyDB tinydb = new TinyDB(getContext());
        tinydb.putObject("concertObj", concert);

    }

    public static void tabFragment(FragmentActivity activity) {
        TabFragment tabFragment = new TabFragment();
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, tabFragment)
                .addToBackStack(null)
                .commit();
    }
    public static void mapFragment(FragmentActivity activity) {
        MapsFragment mapFragment = new MapsFragment();
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, mapFragment)
                .addToBackStack(null)
                .commit();
    }

    public static void loginFragment(FragmentActivity activity) {
        LoginFragment loginFragment = new LoginFragment();
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainLoginFragment, loginFragment)
                .addToBackStack(null)
                .commit();
    }

    public static void signupFragment(FragmentActivity activity) {
        SignupFragment signupFragment = new SignupFragment();
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainLoginFragment, signupFragment)
                .addToBackStack(null)
                .commit();
    }

    public static void resetPasswordFragment(FragmentActivity activity) {
        ResetPasswordFragment resetPasswordFragment = new ResetPasswordFragment();
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainLoginFragment, resetPasswordFragment)
                .addToBackStack(null)
                .commit();
    }

    public static void profileFragment(FragmentActivity activity) {
        ProfileFragment profileFragment = new ProfileFragment();
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, profileFragment)
                .addToBackStack(null)
                .commit();
    }

    public static ConcertFragment concertFragment() {
        return new ConcertFragment();
    }

    public static ConcertInformationFragment concertInformationFragment() {
        return new ConcertInformationFragment();
    }

    public static void filterFragment(FragmentActivity activity,int check) {
        FilterFragment filterFragment = new FilterFragment();
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, filterFragment)
                .addToBackStack(String.valueOf(check))
                .commit();
    }

    public static NotesFragment notesFragment() {
        return new NotesFragment();
    }

    public static void singleEventFragment(FragmentActivity activity) {
        SingleEventFragment singleEventFragment = new SingleEventFragment();
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, singleEventFragment)
                .addToBackStack(null)
                .commit();
    }

    public static VenueInformationFragment venueInformationFragment() {
        return new VenueInformationFragment();

    }

    public void goToEditNote(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("positionOfNote", position);
        EditNotesFragment editNotesFragment = new EditNotesFragment();
        editNotesFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, editNotesFragment)
                .addToBackStack(null)
                .commit();
    }

}

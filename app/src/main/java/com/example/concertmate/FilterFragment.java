package com.example.concertmate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.ArrayList;

public class FilterFragment extends BaseFragment {
    CheckBox alternative , club , country,metal,jazz,rb,rap,rock,pop,tribute;
    Button cancelButton,filterButton;

    public FilterFragment() {
    }

    public static FilterFragment newInstance() {
        return new FilterFragment();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState){
       final View view = inflater.inflate(R.layout.filter_fragment,container,false);
       alternative = view.findViewById(R.id.checkBoxAlternative);
       club = view.findViewById(R.id.checkBoxClub);
       country = view.findViewById(R.id.checkBoxCountry);
       metal = view.findViewById(R.id.checkBoxMetal);
       jazz = view.findViewById(R.id.checkBoxJazz);
       rb = view.findViewById(R.id.checkBoxRB);
       rap = view.findViewById(R.id.checkBoxRap);
       rock = view.findViewById(R.id.checkBoxRock);
       pop = view.findViewById(R.id.checkBoxPop);
       tribute = view.findViewById(R.id.checkBoxTribute);
       cancelButton = view.findViewById(R.id.cancel_filter);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });
        filterButton = view.findViewById(R.id.apply_filter);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    sb.clear();


                    if(alternative !=null && alternative.isChecked()){
                        sb.add("Alternative");
                    }
                    if(club !=null && club.isChecked()){
                        sb.add("club");
                    }
                    if(country !=null && country.isChecked()){
                        sb.add("country");
                    }
                    if(metal !=null && metal.isChecked()){
                        sb.add("metal");
                    }
                    if(jazz !=null && jazz.isChecked()){
                        sb.add("jazz");
                    }
                    if(rb !=null && rb.isChecked()){
                        sb.add("rb");
                    }
                    if(rap !=null && rap.isChecked()){
                        sb.add("rap");
                    }
                    if(rock !=null && rock.isChecked()){
                        sb.add("rock");
                    }
                    if(pop !=null && pop.isChecked()){
                        sb.add("pop");
                    }
                    if(tribute !=null && tribute.isChecked()){
                        sb.add("tribute");
                    }

                    Log.i("INFO",sb.toString());

                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });

        return view;
    }
}

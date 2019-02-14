package com.example.concertmate;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FilterFragment extends BaseFragment {
    CheckBox alternative , club , country,metal,jazz,rb,rap,rock,pop,tribute;
    Button cancelButton,filterButton;
    TextView fromDate ,toDate;
    String  classificationName;

    public FilterFragment() {
    }

    public static FilterFragment newInstance() {
        return new FilterFragment();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState){
       final View view = inflater.inflate(R.layout.filter_fragment,container,false);
        Calendar c = Calendar.getInstance();
        fromDate = view.findViewById(R.id.fromDate);
        toDate= view.findViewById(R.id.toDate);
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
       fromDate.setText(sdf.format(c.getTime()));
       c.add(Calendar.MONTH, 3);
       toDate.setText(sdf.format(c.getTime()));
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
                Bundle bundle = new Bundle();
                    if(alternative !=null && alternative.isChecked()){
                          classificationName=classificationName.concat("alternative,");
                    }
                    if(club !=null && club.isChecked()){
                          classificationName=classificationName.concat("club,");
                    }
                    if(country !=null && country.isChecked()){
                          classificationName=classificationName.concat("country,");
                    }
                    if(metal !=null && metal.isChecked()){
                          classificationName=classificationName.concat("metal,");
                    }
                    if(jazz !=null && jazz.isChecked()){
                          classificationName=classificationName.concat("jazz,");
                    }
                    if(rb !=null && rb.isChecked()){
                          classificationName=classificationName.concat("rb,");
                    }
                    if(rap !=null && rap.isChecked()){
                          classificationName=classificationName.concat("rap,");
                    }
                    if(rock !=null && rock.isChecked()){
                          classificationName=classificationName.concat("rock,");
                    }
                    if(pop !=null && pop.isChecked()){
                          classificationName=classificationName.concat("pop,");
                    }
                    if(tribute !=null && tribute.isChecked()){
                          classificationName=classificationName.concat("tribute");
                    }
                    bundle.putString("classificationName",classificationName);
                TabFragment tabFragment= TabFragment.newInstance(); //get a new Fragment instance
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainFragment, tabFragment)
                        .addToBackStack(null)
                        .commit(); // add it to the current activity

            }
        });

        return view;
    }
}

package com.example.concertmate.Fragments;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.concertmate.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.content.Context.MODE_PRIVATE;

public class FilterFragment extends BaseFragment {
    CheckBox alternative, club, country, metal, jazz, rb, rap, rock, pop, tribute;
    Button cancelButton, filterButton;
    TextView fromDate, toDate;
    String classificationName = "";

    public FilterFragment() {
    }

    public static FilterFragment newInstance() {
        return new FilterFragment();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.filter_fragment, container, false);
        final Calendar c = Calendar.getInstance();
        fromDate = view.findViewById(R.id.fromDate);
        toDate = view.findViewById(R.id.toDate);
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
                TabFragment tabFragment = TabFragment.newInstance();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainFragment, tabFragment)
                        .addToBackStack(null)
                        .commit(); // add it to the current activity
            }
        });

        filterButton = view.findViewById(R.id.apply_filter);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alternative != null && alternative.isChecked()) {
                    classificationName = classificationName.concat("alternative,");
                }
                if (club != null && club.isChecked()) {
                    classificationName = classificationName.concat("club,");
                }
                if (country != null && country.isChecked()) {
                    classificationName = classificationName.concat("country,");
                }
                if (metal != null && metal.isChecked()) {
                    classificationName = classificationName.concat("metal,");
                }
                if (jazz != null && jazz.isChecked()) {
                    classificationName = classificationName.concat("jazz,");
                }
                if (rb != null && rb.isChecked()) {
                    classificationName = classificationName.concat("rb,");
                }
                if (rap != null && rap.isChecked()) {
                    classificationName = classificationName.concat("rap,");
                }
                if (rock != null && rock.isChecked()) {
                    classificationName = classificationName.concat("rock,");
                }
                if (pop != null && pop.isChecked()) {
                    classificationName = classificationName.concat("pop,");
                }
                if (tribute != null && tribute.isChecked()) {
                    classificationName = classificationName.concat("tribute,");
                }
                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

                String source = fromDate.getText().toString();
                String[] sourceSplit = source.split("-");
                int day = Integer.parseInt(sourceSplit[2]);
                int month = Integer.parseInt(sourceSplit[1]);
                int year = Integer.parseInt(sourceSplit[0]);
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.set(day, month - 1, year);
                Date data1 = calendar.getTime();


                source = toDate.getText().toString();
                sourceSplit = source.split("-");
                day = Integer.parseInt(sourceSplit[2]);
                month = Integer.parseInt(sourceSplit[1]);
                year = Integer.parseInt(sourceSplit[0]);
                calendar.set(day, month - 1, year, 23, 59, 59);
                Date data2 = calendar.getTime();
                String dayFormatted = myFormat.format(data1);
                Log.i("INFO", dayFormatted);
                SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("class", classificationName);
                editor.putString("fromDate", myFormat.format(data1));
                editor.putString("toDate", myFormat.format(data2));
                editor.apply();
                TabFragment tabFragment = TabFragment.newInstance();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainFragment, tabFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }


}

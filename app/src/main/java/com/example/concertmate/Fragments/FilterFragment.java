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
import android.widget.Toast;

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
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

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
        fromDate.setText(sdf.format(c.getTime()));
        c.add(Calendar.MONTH, 3);
        toDate.setText(sdf.format(c.getTime()));
        c.add(Calendar.MONTH, -3);
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

        final DatePickerDialog.OnDateSetListener fromDateDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, (monthOfYear));
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateFromLabel(sdf, c);
            }

        };

        final DatePickerDialog.OnDateSetListener toDateDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, (monthOfYear));
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateToLabel(sdf, c);
            }

        };

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String source = fromDate.getText().toString();
                String[] sourceSplit = source.split("-");
                int day = Integer.parseInt(sourceSplit[2]);
                int month = Integer.parseInt(sourceSplit[1]);
                int year = Integer.parseInt(sourceSplit[0]);
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), fromDateDialog, day, month - 1, year).show();
            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String source = toDate.getText().toString();
                String[] sourceSplit = source.split("-");
                int day = Integer.parseInt(sourceSplit[2]);
                int month = Integer.parseInt(sourceSplit[1]);
                int year = Integer.parseInt(sourceSplit[0]);
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), toDateDialog, day, month - 1, year).show();
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
                GregorianCalendar calendar = new GregorianCalendar();

                if (getDateTimeAsDate(calendar, fromDate.getText().toString()).after(getDateTimeAsDate(calendar, toDate.getText().toString()))) {
                    Toast.makeText(getContext(), "From Date is after To Date", Toast.LENGTH_LONG).show();
                } else {

                    SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("class", classificationName);
                    editor.putString("fromDate", myFormat.format(getDateTimeAsDate(calendar, fromDate.getText().toString())));
                    editor.putString("toDate", myFormat.format(getDateTimeAsDate(calendar, toDate.getText().toString())));
                    editor.apply();
                    TabFragment tabFragment = TabFragment.newInstance();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainFragment, tabFragment)
                            .addToBackStack(null)
                            .commit();
                }

            }
        });

        return view;
    }

    public void updateFromLabel(SimpleDateFormat sdf, Calendar c) {
        fromDate.setText(sdf.format(c.getTime()));
    }

    public void updateToLabel(SimpleDateFormat sdf, Calendar c) {
        toDate.setText(sdf.format(c.getTime()));
    }

    public Date getDateTimeAsDate(GregorianCalendar calendar, String dateString) {
        String[] sourceSplit = dateString.split("-");
        int day = Integer.parseInt(sourceSplit[2]);
        int month = Integer.parseInt(sourceSplit[1]);
        int year = Integer.parseInt(sourceSplit[0]);
        calendar.set(day, month - 1, year);
        Date date = calendar.getTime();
        return date;
    }
}

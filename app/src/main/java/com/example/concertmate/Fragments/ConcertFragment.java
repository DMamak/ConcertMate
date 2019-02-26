package com.example.concertmate.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.concertmate.Adapters.FavoriteConcertAdapter;
import com.example.concertmate.Adapters.concertAdapterView;
import com.example.concertmate.Models.Concert;
import com.example.concertmate.Models.Venue;
import com.example.concertmate.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import static android.content.Context.MODE_PRIVATE;

public class ConcertFragment extends BaseFragment {
    concertAdapterView adapter;
    FloatingActionButton fab;
    FavoriteConcertAdapter firebaseRecyclerAdapter;
    ProgressBar pDialog;
    TextView noResults;
    int check;
    //API
    String id, name, date, time, genre, venueName, postCode, address, longitude, latitude, phone, parking, access, subGenre, youtube, twitter, facebook;
    String imageURL = "";
    RequestQueue mRequestQueue;


    public ConcertFragment() {
    }

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        mRequestQueue = Volley.newRequestQueue(getContext());
        final Bundle bundle = this.getArguments();
        check = bundle.getInt("position");
        final Bundle newBundle = new Bundle();
        newBundle.putInt("position", check);
        View view = inflater.inflate(R.layout.recycler_fragment, container, false);
        pDialog = view.findViewById(R.id.progressBar);
        noResults = view.findViewById(R.id.textView3);
        fab = view.findViewById(R.id.fab);
        final SearchView mySearch = view.findViewById(R.id.upcoming_search_view);
        if (check == 0) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    filterFragment(getActivity(),check);
                }
            });
            adapter = new concertAdapterView(concertList, getActivity());
            RecyclerView mRecycler = view.findViewById(R.id.recycler);
            mRecycler.setAdapter(adapter);

            ticketmasterApiRequest("", pDialog);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mRecycler.setLayoutManager(mLayoutManager);
            mySearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    ticketmasterApiRequest(s, pDialog);

                    mySearch.clearFocus();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    if (!StringUtils.isNotEmpty(s)) {
                        ticketmasterApiRequest("", pDialog);
                    }
                    return false;
                }
            });


        } else {
            fab.hide();
            pDialog.setVisibility(View.GONE);
            noResults.setVisibility(View.GONE);
            final Query query;
            query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("concert");

            FirebaseRecyclerOptions<Concert> options =
                    new FirebaseRecyclerOptions.Builder<Concert>()
                            .setQuery(query, Concert.class)
                            .build();

            firebaseRecyclerAdapter = new FavoriteConcertAdapter(options, getActivity());
            firebaseRecyclerAdapter.startListening();
            RecyclerView mRecycler = view.findViewById(R.id.recycler);
            mRecycler.setAdapter(firebaseRecyclerAdapter);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mRecycler.setLayoutManager(mLayoutManager);
            mySearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    firebaseRecyclerAdapter.getFilter().filter(s);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    firebaseRecyclerAdapter.getFilter().filter(s);
                    return false;
                }
            });
        }

        return view;
    }

    public void ticketmasterApiRequest(String s, final ProgressBar pDialog) {
        pDialog.setVisibility(View.VISIBLE);
        s = s.trim();
        s = s.replaceAll("\\s", "%20");


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        StringBuilder url = new StringBuilder();

        url.append("https://app.ticketmaster.com/discovery/v2/events.json?&apikey=").append(getString(R.string.ticketmasterAPI))
                .append("&countryCode=IE").append("&size=200").append("&classificationName=").append(pref.getString("class", "Music"));

        if (StringUtils.isNotBlank(s)) {
            concertList.clear();
            url.append("&keyword=").append(s);
        } else {
            concertList.clear();
            url.append("&startDateTime=").append(pref.getString("fromDate", sdf.format(DateTime.now().withTimeAtStartOfDay().toDate())));
            url.append("&endDateTime=").append(pref.getString("toDate", sdf.format(DateTime.now().plusDays(1).plusMonths(3).withTimeAtStartOfDay().toDate())));
        }
        url.append("&sort=").append("date,asc");

        Log.i("INFO", url.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url.toString(), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray events = response.getJSONObject("_embedded").getJSONArray("events");
                            for (int x = 0; x < events.length(); x++) {

                                JSONObject event = events.getJSONObject(x);
                                id = event.getString("id");
                                name = event.getString("name");
                                final JSONArray images = event.getJSONArray("images");
                                for (int y = 0; y < images.length(); y++) {
                                    if (StringUtils.equalsIgnoreCase(images.getJSONObject(y).getString("width"), "640")) {
                                        imageURL = images.getJSONObject(y).getString("url");
                                    }
                                }
                                date = event.getJSONObject("dates").getJSONObject("start").getString("localDate");
                                time = event.getJSONObject("dates").getJSONObject("start").has("localTime") ? event.getJSONObject("dates").getJSONObject("start").getString("localTime") : "TBD";
                                genre = event.getJSONArray("classifications").getJSONObject(0).getJSONObject("genre").getString("name");
                                subGenre = event.getJSONArray("classifications").getJSONObject(0).getJSONObject("subGenre").getString("name");
                                JSONObject embedded = event.getJSONObject("_embedded");
                                if (embedded.has("attractions")) {
                                    JSONObject attractions = embedded.getJSONArray("attractions").getJSONObject(0);
                                    if (attractions.has("externalLinks")) {
                                        JSONObject links = attractions.getJSONObject("externalLinks");
                                        if (links.has("youtube")) {
                                            youtube = links.getJSONArray("youtube").getJSONObject(0).getString("url");
                                        }
                                        if (links.has("twitter")) {
                                            twitter = links.getJSONArray("twitter").getJSONObject(0).getString("url");
                                        }
                                        if (links.has("facebook")) {
                                            facebook = links.getJSONArray("facebook").getJSONObject(0).getString("url");
                                        }
                                    }
                                }

                                JSONObject venue = event.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0);
                                venueName = venue.getString("name");
                                postCode = (venue.has("postalCode")) ? venue.getString("postalCode") : "N/A";
                                address = (venue.has("address") && venue.getJSONObject("address").has("line1"))
                                        ? venue.getJSONObject("address").getString("line1") : "N/A";
                                longitude = venue.has("location") && venue.getJSONObject("location").has("longitude") ? venue.getJSONObject("location").getString("longitude") : "N/A";
                                latitude = venue.has("location") && venue.getJSONObject("location").has("latitude") ? venue.getJSONObject("location").getString("latitude") : "N/A";
                                phone = (venue.has("boxOfficeInfo") && venue.getJSONObject("boxOfficeInfo").has("phoneNumberDetail"))
                                        ? venue.getJSONObject("boxOfficeInfo").getString("phoneNumberDetail") : "N/A";

                                parking = venue.has("parkingDetail") ? venue.getString("parkingDetail") : "N/A";
                                access = (venue.has("accessibleSeatingDetail")) ? venue.getString("accessibleSeatingDetail") : "N/A";
                                concertList.add(new Concert(id, name, imageURL, date, time, genre, subGenre,
                                        new Venue(venueName, postCode, address, longitude, latitude, phone, parking, access), false, youtube, twitter, facebook));
                            }
                            noResults.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                            pDialog.setVisibility(View.GONE);
                        } catch (Exception e) {
                            Log.e("Parse_Error", e.toString());
                            pDialog.setVisibility(View.GONE);
                            noResults.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley_Error", error.toString());
                    }
                });

        mRequestQueue.add(jsonObjectRequest);
    }
}

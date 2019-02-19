package com.example.concertmate.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.concertmate.Adapters.concertAdapterView;
import com.example.concertmate.Models.Concert;
import com.example.concertmate.Models.Venue;
import com.example.concertmate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class BaseFragment extends Fragment {
    ArrayList<Concert> concertList = new ArrayList<>();
    String id, name, date, time, genre, venueName, postCode, address, longitude, latitude, phone, parking, access;
    String imageURL = "";

    public void ticketmasterApiRequest(Context context, final concertAdapterView adapter, Calendar c, String s) {
        String test = "Music";
        s = s.trim();
        s = s.replaceAll("\\s", "%20");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        StringBuilder url = new StringBuilder();
        RequestQueue mRequestQueue;
        mRequestQueue = Volley.newRequestQueue(context);

        url.append("https://app.ticketmaster.com/discovery/v2/events.json?&apikey=").append(getString(R.string.ticketmasterAPI))
                .append("&countryCode=IE").append("&size=200").append("&classificationName=").append(pref.getString("class", test));
        c.add(Calendar.MONTH, 3);
        if (StringUtils.isNotBlank(s)) {
            concertList.clear();
            url.append("&keyword=").append(s);
        } else {
            concertList.clear();
            url.append("&startDateTime=").append(pref.getString("fromDate", sdf.format(c.getTime())))
                    .append("&endDateTime=").append(pref.getString("toDate", sdf.format(c.getTime())));
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
                                concertList.add(new Concert(id, name, imageURL, date, time, genre,
                                        new Venue(venueName, postCode, address, longitude, latitude, phone, parking, access), false));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            Log.e("Parse_Error", e.toString());
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

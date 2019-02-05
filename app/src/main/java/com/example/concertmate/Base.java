package com.example.concertmate;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.concertmate.Models.Concert;

import org.apache.commons.lang3.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Base extends AppCompatActivity {
    List<Concert> concertList = new ArrayList<>();

    public void ticketmasterApiRequest(final RequestQueue mRequestQueue) {
        String url = "https://app.ticketmaster.com/discovery/v2/events.json?&apikey="+getString(R.string.ticketmasterAPI)+"&countryCode=IE&classificationName=Music&size=100";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String name;
                        String imageURL = "";
                        String date;
                        String time;
                        String genre;
                        try {
                            JSONArray events = response.getJSONObject("_embedded").getJSONArray("events");
                            for (int x = 0; x < events.length(); x++) {
                                JSONObject event = events.getJSONObject(x);
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
                                concertList.add(new Concert(name, imageURL, date, time, genre));
                            }

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

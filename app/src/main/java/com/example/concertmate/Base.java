package com.example.concertmate;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;


public class Base extends AppCompatActivity {

    public void ticketmasterApiRequest(RequestQueue mRequestQueue) {
        String url = "https://app.ticketmaster.com/discovery/v2/events.json?size=200&apikey=HKCzIJYq62sNm6UdqKlqHcDAMJAzQ4RT&countryCode=IE&classificationName=Music";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray events = response.getJSONObject("_embedded").getJSONArray("events");
                            for (int x = 0; x < events.length(); x++) {
                                JSONObject event = events.getJSONObject(x);
                                Log.i("INFO", event.toString());
                            }

                        } catch (Exception e) {
                            Log.e("ERROR", e.toString());
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", error.toString());

                    }
                });

        mRequestQueue.add(jsonObjectRequest);
    }

}

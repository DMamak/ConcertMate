package com.example.concertmate;

import android.content.Context;
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

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class BaseFragment extends Fragment {


    ArrayList<Concert> concertList = new ArrayList<>();
    String fromDate,toDate,classificationName;


    public void ticketmasterApiRequest(Context context,final concertAdapterView adapter) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        fromDate=sdf.format(c.getTime());
        c.add(Calendar.MONTH, 3);
        toDate=sdf.format(c.getTime());

        RequestQueue mRequestQueue;

        mRequestQueue = Volley.newRequestQueue(context);
            concertList.clear();

        String url = "https://app.ticketmaster.com/discovery/v2/events.json?&apikey=" + getString(R.string.ticketmasterAPI) + "&countryCode=IE&size=200";
        url = url.concat("&classificationName=").concat(classificationName);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String name, date, time, genre, venueName, postCode, address, longitude, latitude, phone, parking, access;
                        String imageURL = "";
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
                                concertList.add(new Concert(name, imageURL, date, time, genre,
                                        new Venue(venueName, postCode, address, longitude, latitude, phone, parking, access)));
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

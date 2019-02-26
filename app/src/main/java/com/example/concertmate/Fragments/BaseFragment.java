package com.example.concertmate.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import com.example.concertmate.Utils.TinyDB;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class BaseFragment extends Fragment {
    ArrayList<Concert> concertList = new ArrayList<>();
    String id, name, date, time, genre, venueName, postCode, address, longitude, latitude, phone, parking, access ,subGenre,youtube,twitter,facebook;
    String imageURL = "";
    // list of fragments;
    static ConcertFragment upcomingConcertFragment;
    static ConcertFragment concertFragment;
    static ConcertInformationFragment concertInformationFragment;
    static FilterFragment filterFragment;
    static NotesFragment notesFragment;
    static SingleEventFragment singleEventFragment;
    static TabFragment tabFragment;
    static VenueInformationFragment venueInformationFragment;

    public void ticketmasterApiRequest(Context context, final concertAdapterView adapter, String s) {
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

        if (StringUtils.isNotBlank(s)) {
            concertList.clear();
            url.append("&keyword=").append(s);
        } else {
            concertList.clear();
            url.append("&startDateTime=").append(pref.getString("fromDate", sdf.format(DateTime.now().toDate())));
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
                                if(embedded.has("attractions")) {
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
                                concertList.add(new Concert(id, name, imageURL, date, time, genre,subGenre,
                                        new Venue(venueName, postCode, address, longitude, latitude, phone, parking, access), false,youtube,twitter,facebook));
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

    public static void initializeFragments(){
        upcomingConcertFragment= new ConcertFragment();
        concertFragment = new ConcertFragment();
        concertInformationFragment = new ConcertInformationFragment();
        filterFragment= new FilterFragment();
        notesFragment = new NotesFragment();
        singleEventFragment = new SingleEventFragment();
        tabFragment = new TabFragment();
        venueInformationFragment = new VenueInformationFragment();
    }
    public Concert getJsonConcert(Context context){
        TinyDB tinydb = new TinyDB(context);
        return   tinydb.getObject("concertObj",Concert.class);
    }

    public void saveJsonConcert(Concert concert){
        TinyDB tinydb = new TinyDB(getContext());
        tinydb.putObject("concertObj",concert);

    }

    public int getPosition(Context context){
        TinyDB tinydb = new TinyDB(context);
        return   tinydb.getInt("positionOfNote");
    }

    public static void tabFragment(FragmentActivity activity){
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, tabFragment)
                .addToBackStack(null)
                .commit();
    }

    public static void concertFragment(FragmentActivity activity){
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, upcomingConcertFragment)
                .addToBackStack(null)
                .commit();
    }

    public static void UpcomingConcertFragment(FragmentActivity activity){
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, concertFragment)
                .addToBackStack(null)
                .commit();
    }

    public static void ConcertInformationFragment(FragmentActivity activity){
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, concertInformationFragment)
                .addToBackStack(null)
                .commit();
    }

    public static void filterFragment(FragmentActivity activity){
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, filterFragment)
                .addToBackStack(null)
                .commit();
    }

    public static void notesFragment(FragmentActivity activity){
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, notesFragment)
                .addToBackStack(null)
                .commit();
    }

    public static void singleEventFragment(FragmentActivity activity){
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, singleEventFragment)
                .addToBackStack(null)
                .commit();
    }

    public static void venueInformationFragment(FragmentActivity activity){
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, venueInformationFragment)
                .addToBackStack(null)
                .commit();
    }

    public void goToEditNote(int position){
        Bundle bundle = new Bundle();
        bundle.putInt("positionOfNote",position);
        EditNotesFragment editNotesFragment = new EditNotesFragment();
        editNotesFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, editNotesFragment)
                .addToBackStack(null)
                .commit();
    }

}

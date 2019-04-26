package com.example.concertmate.Fragments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.concertmate.Adapters.MarkerInfoAdapter;
import com.example.concertmate.Adapters.mapEventRecycler;
import com.example.concertmate.Models.Concert;
import com.example.concertmate.Models.Venue;
import com.example.concertmate.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsFragment extends BaseFragment implements OnMapReadyCallback {
    String id, name, date, time, genre, venueName, postCode, address, longitude, latitude, phone, parking, access, subGenre, youtube, twitter, facebook
            ,venueId,venueUrl;
    String imageURL = "";
    RecyclerView mapRecycler;
    double lat;
    double lng;
    RequestQueue mRequestQueue;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private GoogleMap mMap;
    mapEventRecycler mapEventRecyclerAdapter;
    ArrayList<Concert> concertArrayList;

    public MapsFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_maps, container, false);
        mRequestQueue = Volley.newRequestQueue(getContext());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapRecycler = view.findViewById(R.id.map_recyclerView);
        mapRecycler.setVisibility(View.GONE);
        mapFragment.getMapAsync(this);

        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("concert").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for( DataSnapshot snapshot : dataSnapshot.getChildren()){
                 if(Boolean.valueOf(snapshot.child("attending").getValue().toString())){
                     Venue venue =snapshot.child("venue").getValue(Venue.class);
                     lat = Double.valueOf(venue.getLatitude());
                     lng = Double.valueOf(venue.getLongitude());
                     LatLng latLng = new LatLng(lat, lng);
                     MarkerInfoAdapter adapter = new MarkerInfoAdapter(getActivity(),mapRecycler);
                     mMap.setInfoWindowAdapter(adapter);
                     MarkerOptions markerOptions = new MarkerOptions();
                     markerOptions.position(latLng);
                     Marker marker = mMap.addMarker(markerOptions);
                     marker.setTag(venue);
                 }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                concertArrayList=new ArrayList<>();
                mapRecycler.setVisibility(View.VISIBLE);
                apiCall((Venue)marker.getTag());
                mapEventRecyclerAdapter = new mapEventRecycler(concertArrayList,getActivity());
                mapRecycler.setAdapter(mapEventRecyclerAdapter);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                mapRecycler.setLayoutManager(mLayoutManager);
                return false;
            }
        });
       mMap.getUiSettings().setMapToolbarEnabled(false);
       mMap.setOnInfoWindowCloseListener(new GoogleMap.OnInfoWindowCloseListener() {
           @Override
           public void onInfoWindowClose(Marker marker) {
               mapRecycler.setVisibility(View.GONE);
           }
       });
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(53.350140,-6.266155) , 13.0f) );
    }


    public void apiCall(Venue venue){
        StringBuilder url = new StringBuilder();
        url.append("https://app.ticketmaster.com/discovery/v2/events.json?apikey=").append(getString(R.string.ticketmasterAPI));
        url.append("&venueId=").append(venue.getId()).append("&size=20").append("&classificationName=Music").append("&sort=name,date,desc");
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
                                if(event.has("classifications")){
                                    if(event.getJSONArray("classifications").getJSONObject(0).has("genre")){
                                        genre = event.getJSONArray("classifications").getJSONObject(0).getJSONObject("genre").getString("name");
                                    }
                                }
                                subGenre =(event.getJSONArray("classifications").getJSONObject(0).has("subGenre"))? event.getJSONArray("classifications").getJSONObject(0).getJSONObject("subGenre").getString("name") : "N/A";
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
                                venueId = venue.getString("id");
                                if(venue.has("images")){
                                    if(venue.getJSONArray("images").length()>0){
                                        venueUrl =  venue.getJSONArray("images").getJSONObject(0).getString("url");
                                    }else{
                                        venueUrl="n/a";
                                    }
                                }else{
                                    venueUrl="n/a";
                                }
                                postCode = (venue.has("postalCode")) ? venue.getString("postalCode") : "N/A";
                                address = (venue.has("address") && venue.getJSONObject("address").has("line1"))
                                        ? venue.getJSONObject("address").getString("line1") : "N/A";
                                longitude = venue.has("location") && venue.getJSONObject("location").has("longitude") ? venue.getJSONObject("location").getString("longitude") : "N/A";
                                latitude = venue.has("location") && venue.getJSONObject("location").has("latitude") ? venue.getJSONObject("location").getString("latitude") : "N/A";
                                phone = (venue.has("boxOfficeInfo") && venue.getJSONObject("boxOfficeInfo").has("phoneNumberDetail"))
                                        ? venue.getJSONObject("boxOfficeInfo").getString("phoneNumberDetail") : "N/A";

                                parking = venue.has("parkingDetail") ? venue.getString("parkingDetail") : "N/A";
                                access = (venue.has("accessibleSeatingDetail")) ? venue.getString("accessibleSeatingDetail") : "N/A";
                                concertArrayList.add(new Concert(id, name, imageURL, date, time, genre, subGenre,
                                        new Venue(venueId,venueUrl,venueName, postCode, address, longitude, latitude, phone, parking, access), false, youtube, twitter, facebook));
                                mapEventRecyclerAdapter.notifyDataSetChanged();
                            }
                            mapEventRecyclerAdapter.notifyDataSetChanged();
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


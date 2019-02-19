package com.example.concertmate.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Concert {
    private String Id;
    private String name;
    private String imageURL;
    private String date;
    private String time;
    private String genre;
    private Venue venue;
    private boolean favorite;

    public Concert(String name, String imageURL, String date, String time, String genre,Venue venue) {
        this.name = name;
        this.imageURL = imageURL;
        this.date = date;
        this.time = time;
        this.genre = genre;
        this.venue = venue;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Venue getVenue() {
        return venue;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}

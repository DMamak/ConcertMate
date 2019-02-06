package com.example.concertmate.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Concert implements Parcelable {
    private String name;
    private String imageURL;
    private String date;
    private String time;
    private String genre;
    private Venue venue;

    public Concert(String name, String imageURL, String date, String time, String genre,Venue venue) {
        this.name = name;
        this.imageURL = imageURL;
        this.date = date;
        this.time = time;
        this.genre = genre;
        this.venue = venue;
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

    protected Concert(Parcel in) {
        name = in.readString();
        imageURL = in.readString();
        date = in.readString();
        time = in.readString();
        genre = in.readString();
        venue = (Venue) in.readValue(Venue.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imageURL);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(genre);
        dest.writeValue(venue);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Concert> CREATOR = new Parcelable.Creator<Concert>() {
        @Override
        public Concert createFromParcel(Parcel in) {
            return new Concert(in);
        }

        @Override
        public Concert[] newArray(int size) {
            return new Concert[size];
        }
    };
}

package com.example.concertmate.Models;

import java.util.ArrayList;

public class Concert {
    private String Id;
    private String name;
    private String imageURL;
    private String date;
    private String time;
    private String genre;
    private String subGenre;
    private String youtubeLink;
    private String facebookLink;
    private String twitterLink;
    private Venue venue;
    private boolean favorite;
    private boolean attending;
    private ArrayList<Notes> notesArrayList = new ArrayList<>();

    public Concert(String id, String name, String imageURL, String date, String time, String genre, String subGenre, Venue venue, boolean favorite, String youtube, String twitter, String facebook) {
        this.Id = id;
        this.name = name;
        this.imageURL = imageURL;
        this.date = date;
        this.time = time;
        this.genre = genre;
        this.subGenre = subGenre;
        this.venue = venue;
        this.favorite = favorite;
        this.youtubeLink = youtube;
        this.twitterLink = twitter;
        this.facebookLink = facebook;
    }

    public Concert() {
    }

    public boolean isAttending() {
        return attending;
    }

    public void setAttending(boolean attending) {
        this.attending = attending;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public String getTwitterLink() {
        return twitterLink;
    }

    public void setTwitterLink(String twitterLink) {
        this.twitterLink = twitterLink;
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

    public String getSubGenre() {
        return subGenre;
    }

    public void setSubGenre(String subGenre) {
        this.subGenre = subGenre;
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

    public ArrayList<Notes> getNotesArrayList() {
        return notesArrayList;
    }

    public void setNotesArrayList(ArrayList<Notes> notesArrayList) {
        this.notesArrayList = notesArrayList;
    }
}

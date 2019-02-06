package com.example.concertmate.Models;

public class Concert {
    private String name;
    private String imageURL;
    private String date;
    private String time;
    private String genre;
    private String venue;

    public Concert(String name, String imageURL, String date, String time, String genre) {
        this.name = name;
        this.imageURL = imageURL;
        this.date = date;
        this.time = time;
        this.genre = genre;
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

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }
}

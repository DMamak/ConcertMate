package com.example.concertmate.tests;
import org.junit.*;

import com.example.concertmate.Models.Concert;
import com.example.concertmate.Models.Venue;

import junit.framework.TestCase;

public class ConcertTest extends TestCase{

    private Concert concert;

    protected void setUp() throws Exception {
        super.setUp();
        concert = new Concert();
    }

    public void testVenues() {
        concert.toString();
    }

    public void testGetId() {
        String expected = String.valueOf(Math.random());
        concert.setId(expected);
        String actual = concert.getId();
        Assert.assertEquals(expected, actual);
    }

    public void testGetName() {
        String expected = String.valueOf(Math.random());
        concert.setName(expected);
        String actual = concert.getName();
        Assert.assertEquals(expected, actual);
    }

    public void testGetImageUrl() {
        String expected = String.valueOf(Math.random());
        concert.setImageURL(expected);
        String actual = concert.getImageURL();
        Assert.assertEquals(expected, actual);
    }

    public void testGetDate() {
        String expected = String.valueOf(Math.random());
        concert.setDate(expected);
        String actual = concert.getDate();
        Assert.assertEquals(expected, actual);
    }

    public void testGetTime() {
        String expected = String.valueOf(Math.random());
        concert.setTime(expected);
        String actual = concert.getTime();
        Assert.assertEquals(expected, actual);
    }

    public void testGetGenre() {
        String expected = String.valueOf(Math.random());
        concert.setGenre(expected);
        String actual = concert.getGenre();
        Assert.assertEquals(expected, actual);
    }

    public void testGetSubGenre() {
        String expected = String.valueOf(Math.random());
        concert.setSubGenre(expected);
        String actual = concert.getSubGenre();
        Assert.assertEquals(expected, actual);
    }

    public void testGetYoutubeLink() {
        String expected = String.valueOf(Math.random());
        concert.setYoutubeLink(expected);
        String actual = concert.getYoutubeLink();
        Assert.assertEquals(expected, actual);
    }

    public void testGetFacebookLink() {
        String expected = String.valueOf(Math.random());
        concert.setFacebookLink(expected);
        String actual = concert.getFacebookLink();
        Assert.assertEquals(expected, actual);
    }

    public void testGetTwitterLink() {
        String expected = String.valueOf(Math.random());
        concert.setTwitterLink(expected);
        String actual = concert.getTwitterLink();
        Assert.assertEquals(expected, actual);
    }
    public void testGetVenue() {
        Venue expected = new Venue();
        concert.setVenue(expected);
        Venue actual = concert.getVenue();
        Assert.assertEquals(expected, actual);
    }
    public void testGetFavorite() {
        boolean expected = true;
        concert.setFavorite(expected);
        Boolean actual = concert.isFavorite();
        Assert.assertEquals(expected, actual);
    }
    public void testGetAttending() {
        boolean expected = true;
        concert.setAttending(expected);
        Boolean actual = concert.isAttending();
        Assert.assertEquals(expected, actual);
    }
}
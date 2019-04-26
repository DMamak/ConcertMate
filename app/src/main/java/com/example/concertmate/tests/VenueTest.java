package com.example.concertmate.tests;

import com.example.concertmate.Models.Venue;

import junit.framework.TestCase;

import org.junit.Assert;

public class VenueTest extends TestCase {

    private Venue venue;

    protected void setUp() throws Exception {
        super.setUp();
        venue = new Venue();
    }

    public void testVenues() {
        venue.toString();
    }

    public void testGetId() {
        String expected = String.valueOf(Math.random());
        venue.setId(expected);
        String actual = venue.getId();
        Assert.assertEquals(expected, actual);
    }

    public void testGetUrl() {
        String expected = String.valueOf(Math.random());
        venue.setUrl(expected);
        String actual = venue.getUrl();
        Assert.assertEquals(expected, actual);
    }

    public void testGetVenueName() {
        String expected = String.valueOf(Math.random());
        venue.setVenueName(expected);
        String actual = venue.getVenueName();
        Assert.assertEquals(expected, actual);
    }

    public void testGetPostcode() {
        String expected = String.valueOf(Math.random());
        venue.setPostCode(expected);
        String actual = venue.getPostCode();
        Assert.assertEquals(expected, actual);
    }

    public void testGetAddress() {
        String expected = String.valueOf(Math.random());
        venue.setAddress(expected);
        String actual = venue.getAddress();
        Assert.assertEquals(expected, actual);
    }

    public void testGetLong() {
        String expected = String.valueOf(Math.random());
        venue.setLongitude(expected);
        String actual = venue.getLongitude();
        Assert.assertEquals(expected, actual);
    }

    public void testGetLat() {
        String expected = String.valueOf(Math.random());
        venue.setLatitude(expected);
        String actual = venue.getLatitude();
        Assert.assertEquals(expected, actual);
    }

    public void testGetPhonenumber() {
        String expected = String.valueOf(Math.random());
        venue.setPhoneNumber(expected);
        String actual = venue.getPhoneNumber();
        Assert.assertEquals(expected, actual);
    }

    public void testGetParking() {
        String expected = String.valueOf(Math.random());
        venue.setParking(expected);
        String actual = venue.getParking();
        Assert.assertEquals(expected, actual);
    }

    public void testGetAccessible() {
        String expected = String.valueOf(Math.random());
        venue.setAccessible(expected);
        String actual = venue.getAccessible();
        Assert.assertEquals(expected, actual);
    }
}
